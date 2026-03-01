import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-callback',
  standalone: true,
  imports: [],
  templateUrl: './auth-callback.component.html',
  styleUrl: './auth-callback.component.scss',
})
export class AuthCallbackComponent implements OnInit {
  private router = inject(Router);

  ngOnInit(): void {
    const queryParams = new URLSearchParams(window.location.search);
    const hashParams = new URLSearchParams(
      (window.location.hash || '').replace(/^#/, ''),
    );
    const token = queryParams.get('token') || hashParams.get('token');

    if (token) {
      localStorage.setItem('token', token);

      window.history.replaceState({}, document.title, '/auth/callback')
    }

    this.router.navigateByUrl('/')
  }
}
