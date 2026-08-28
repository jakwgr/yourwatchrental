export function onlyNumbers(event: Event) {
  const input = event.target as HTMLInputElement;
  input.value = input.value.replace(/[^0-9]/g, '');
}

// onlyNumbers(event: Event)
//   {
//     onlyNumbers(event);
//   }

//  maxlength="9" (input)="onlyNumbers($event)"