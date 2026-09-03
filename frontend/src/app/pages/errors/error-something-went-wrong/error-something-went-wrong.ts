import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
    selector: 'app-error-something-went-wrong',
    imports: [RouterLink],
    templateUrl: './error-something-went-wrong.html',
    styleUrl: './error-something-went-wrong.css',
})
export class ErrorSomethingWentWrong implements OnInit {

    constructor(private router: Router) {}

    ngOnInit(): void {
        const navigation =
            performance.getEntriesByType('navigation')[0] as PerformanceNavigationTiming;

        if (navigation.type === 'reload') {
            this.router.navigate(['/']);
        }
    }
}