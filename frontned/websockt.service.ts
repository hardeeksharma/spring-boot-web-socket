import {Injectable} from '@angular/core';
import {share} from 'rxjs/operators';
import {Subject} from 'rxjs/internal/Subject';
import {Observable} from 'rxjs/internal/Observable';
import {Observer} from 'rxjs/internal/types';@Injectable({
   providedIn: 'root'
})
export class WebsocketService {
   public messages: Subject<any>;
   public ws: any;
   private subject: Subject<MessageEvent>;    constructor() {
   }    public connect(url: string): Subject<MessageEvent> {
       if (!this.subject) {
           this.subject = this.create(url);
       }
       return this.subject;
   }    public close() {
       if (this.ws) {
           this.ws.close();
           this.subject = null;
       }
   }    private create(url: string): Subject<MessageEvent> {
       this.ws = new WebSocket(url);
       const observable = new Observable(
           (obs: Observer<MessageEvent>) => {
               this.ws.onmessage = obs.next.bind(obs);
               this.ws.onerror = obs.error.bind(obs);
               this.ws.onclose = obs.complete.bind(obs);
               return this.ws.close.bind(this.ws);
           }).pipe(share());
       const observer = {
           next: (data: Object) => {
               if (this.ws.readyState === WebSocket.OPEN) {
                   this.ws.send(JSON.stringify(data));
               }
           }        };
       return Subject.create(observer, observable);
   }
}