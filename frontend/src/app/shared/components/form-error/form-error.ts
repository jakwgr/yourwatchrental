import { Component, DestroyRef, effect, inject, input, signal } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
    selector: 'app-form-error',
    standalone: true,
    templateUrl: './form-error.html'
})
export class FormError {

    control = input.required<AbstractControl>();

    private destroyRef = inject(DestroyRef);
patternMessage = input<string | null>(null);
    controlChanged = signal(0);
    maxValue = input<number|null|string>(null)

    constructor() {
        effect(() => {
            const control = this.control();

            control.events
                .pipe(takeUntilDestroyed(this.destroyRef))
                .subscribe(() => {
                    this.controlChanged.update(value => value + 1);
                });
        });
    }
}