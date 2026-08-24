import { Component,HostListener,inject,OnDestroy,OnInit,PLATFORM_ID,signal} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';

import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchCardResponseDTO } from '../../core/models/watches/watch-card-response.dto';
import { WatchCard } from '../../shared/components/watch-card-view/watch-card-view';
@Component({
  selector: 'app-home',
  imports: [RouterLink, WatchCard],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit, OnDestroy {

  private platformId = inject(PLATFORM_ID);
  private watchesService = inject(WatchesService);
  slides = [
    {
      eyebrow: 'Haute Horlogerie',
      title: 'Czas w Twoich rękach',
      description: 'Odkryj kolekcję najbardziej ekskluzywnych czasomierzy świata. Wynajmuj bez kompromisów, nosząc precyzję najwyższej klasy.',
      buttonText: 'Poznaj kolekcję',
      buttonLink: '/watches',
      bgImage: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=1920&auto=format&fit=crop'
    },
    {
      eyebrow: 'Mistrzowska precyzja',
      title: 'Szwajcarskie ikony',
      description: 'Od klasycznych automatów po nowoczesne komplikacje. Wybierz model, który podkreśli Twój styl i status.',
      buttonText: 'Zobacz nowości',
      buttonLink: '/watches/new',
      bgImage: 'https://images.unsplash.com/photo-1542496658-e33a6d0d50f6?q=80&w=1920&auto=format&fit=crop'
    },
    {
      eyebrow: 'Ekskluzywna oferta',
      title: 'Limitowane edycje',
      description: 'Dostęp do rzadkich i poszukiwanych modeli. Poczuj wyjątkowość marki na własnym nadgarstku.',
      buttonText: 'Aplikuj do klubu',
      buttonLink: '/membership',
      bgImage: 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?q=80&w=1920&auto=format&fit=crop'
    }
  ];
watches = signal<WatchCardResponseDTO[]>([]);
  currentIndex = signal(0);
  private intervalId: ReturnType<typeof setInterval> | undefined;
  scrollY = signal(0);


    loadWatches()
  {

    this.watchesService.getWatches(0, 3).subscribe(response => {

      this.watches.set(response.content);
    }
  )
  }

@HostListener('window:scroll')
onScroll() {
    this.scrollY.set(window.scrollY);
}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.startAutoSlide();
    }

    this.loadWatches();
  }

  ngOnDestroy(): void {
    this.stopAutoSlide();
  }

  goToSlide(index: number): void {
    this.currentIndex.set(index);

    this.stopAutoSlide();
    this.startAutoSlide();
  }

  private startAutoSlide(): void {
    this.intervalId = setInterval(() => {
      this.nextSlide();
    }, 7000);
  }

  private stopAutoSlide(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
      this.intervalId = undefined;
    }
  }

  private nextSlide(): void {
    this.currentIndex.update(index =>
      (index + 1) % this.slides.length
    );
  }
}