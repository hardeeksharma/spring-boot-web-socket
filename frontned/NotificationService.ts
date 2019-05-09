import {Injectable} from '@angular/core';
import {WebsocketService} from './websocket.service';

import {AppService} from '../../app.service';
import {DeleteAll, DeleteNotification, Message, NotificationList, WebSocketMessage} from './notificationinterfaces';
import * as jwt_decode from 'jwt-decode';
import {map} from 'rxjs/operators';
import {Subject} from 'rxjs/internal/Subject';
import {Router} from '@angular/router';

declare var toastr: any;

@Injectable({
    providedIn: 'root'
})
export class NotificationService {
    public notifications: Subject<NotificationList>;
    public delete: Subject<DeleteNotification>;
    public deleteAll: Subject<DeleteAll>;
    dashboardIndexvalue: number;
    notificationCategory: string;


    constructor(private wsService: WebsocketService,
                private appService: AppService,
                private router: Router) {
    }

    getDecodedAccessToken(token: string): string {
        try {
            return jwt_decode(token).registrationKey;
        } catch (Error) {
            return null;
        }
    }

    getNotifications() {
        if (localStorage.getItem('currentUser')) {
            const token = JSON.parse(localStorage.getItem('currentUser')).jsonWebToken;
            const registrationKey = this.getDecodedAccessToken(token);
            const CHAT_URL = this.appService.webSocketUrl + registrationKey;
            this.notifications = <Subject<NotificationList>>this.wsService
                .connect(CHAT_URL)
                .pipe(map((response: MessageEvent): Message => {
                    return JSON.parse(response.data);
                }));
        }
    }

    displayToastr(newNotification: NotificationList) {
        this.dashboardIndexvalue = this.getCurrentLocation().search('dashboard/');

        if (this.dashboardIndexvalue > 0) {
            this.notificationCategory = newNotification.userNotification[0].category;
            if (this.notificationCategory === 'info') {
                toastr.options.onclick = () => {
                    this.router.navigate(['/dashboard']);
                };
                toastr.info(newNotification.userNotification[0].message, 'Notification');
            } else if (this.notificationCategory === 'error') {
                toastr.options.onclick = () => {
                    this.router.navigate(['/dashboard']);
                };
                toastr.error(newNotification.userNotification[0].message, 'Notification');

            } else if (this.notificationCategory === 'warn') {
                toastr.options.onclick = () => {
                    this.router.navigate(['/dashboard']);
                };
                toastr.warning(newNotification.userNotification[0].message, 'Notification');
            }
        }
    }

    getCurrentLocation() {
        return window.location.href;
    }

    public close() {
        this.wsService.close();
    }

    send(data: WebSocketMessage) {
        this.notifications.next(data);
    }
}