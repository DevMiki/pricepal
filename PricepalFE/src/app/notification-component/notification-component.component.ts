import { Component, OnDestroy } from '@angular/core';
import { Notification } from '@models/notification';
import { NotificationService } from '@services/notification.service';

@Component({
  selector: 'app-notification-component',
  standalone: true,
  imports: [],
  templateUrl: './notification-component.component.html',
  styleUrl: './notification-component.component.scss',
})
export class NotificationComponentComponent implements OnDestroy {
  currentNotification: Notification | null = null;
  private timerId: ReturnType<typeof setTimeout> | null = null;
  private subscription = this.notificationService.current$.subscribe(
    (notification) => {
      this.clearTimer();
      this.currentNotification = notification;
      if (this.currentNotification) {
        this.startAutoHide();
      }
    }
  );

  constructor(private notificationService: NotificationService) {}

  private startAutoHide() {
    this.timerId = setTimeout(
      () => this.notificationService.completeCurrent(),
      3000
    );
  }

  clearTimer() {
    if (this.timerId) {
      clearTimeout(this.timerId);
      this.timerId = null;
    }
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    this.clearTimer();
  }
}
