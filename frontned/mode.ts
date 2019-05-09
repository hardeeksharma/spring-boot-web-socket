export interface Message {
    action: string;
}

export interface NotificationList extends Message {
    userNotification?: NotificationOfUser [];
    length?: number;
    id?: any;
    isDeleted?: boolean;
}

export interface NotificationOfUser {
    notificationID: string;
    message: string;
    category: string;
    timeStamp: string;
}

export interface DeleteNotification extends Message {
    id: any;
    isDeleted: boolean;
}

export interface DeleteAll extends Message {
    isAllDeleted: boolean;
    action: string;
}

export interface WebSocketMessage extends Message {
    message: string;
}