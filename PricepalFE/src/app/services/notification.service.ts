import { Injectable } from "@angular/core";
import { Notification } from "@models/notification";
import { BehaviorSubject } from "rxjs";

@Injectable({providedIn: 'root'})
export class NotificationService {

    private queue: Notification[] = [];
    private currentSubject = new BehaviorSubject<Notification | null>(null);
    current$ = this.currentSubject.asObservable();

    notifySuccess(message: string){
        const newNotification: Notification = {id: crypto.randomUUID(), message, type: 'success'}
        this.enqueue(newNotification);
        this.startQueueIfIdle();
    }

    completeCurrent(){
        this.showNext();
    }
    
    private enqueue(notification: Notification) {
        this.queue.push(notification);
    }
    
    private startQueueIfIdle(){
        if(!this.currentSubject.value){
            this.showNext();
        }
    }

    private showNext(){
        const next = this.queue.shift() ?? null;
        this.currentSubject.next(next);
    }
}