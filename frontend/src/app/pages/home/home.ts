import { Component,HostListener,inject,OnDestroy,OnInit,PLATFORM_ID,signal} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';

import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchCardResponseDTO } from '../../core/models/watches/watch-card-response.dto';
import { WatchCard } from '../../shared/components/watch-card-view/watch-card-view';
import { PortfolioProjectAlert1 } from '../../shared/components/portfolio-project-alert-1/portfolio-project-alert-1';
import { PortfolioProjectAlert2 } from '../../shared/components/portfolio-project-alert-2/portfolio-project-alert-2';


@Component({
  selector: 'app-home',
  imports: [RouterLink, WatchCard, PortfolioProjectAlert2, PortfolioProjectAlert1],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit, OnDestroy {

  private platformId = inject(PLATFORM_ID);
  private watchesService = inject(WatchesService);
slides = [

  {
    eyebrow: 'Haute Horlogerie',
    title: 'Time in Your Hands',
    description: 'Discover a collection of the world’s most exclusive timepieces. Rent without compromise and experience precision at its finest.',
    buttonText: 'Explore the Collection',
    buttonLink: '/watches',
    bgImage: '/static_photos/photo_slider_3.jpg'
  },

  {
    eyebrow: 'Masterful Precision',
    title: 'Swiss Icons',
    description: 'From classic automatic watches to modern complications. Choose a timepiece that reflects your style and status.',
    buttonText: 'Discover New Arrivals',
    buttonLink: '/watches',
    bgImage: '/static_photos/photo_slider_2.jpg'
  },

  {
    eyebrow: 'Exclusive Selection',
    title: 'Limited Editions',
    description: 'Gain access to rare and sought-after timepieces. Experience the exclusivity of iconic brands on your wrist.',
    buttonText: 'Let`s start the journey',
    buttonLink: '/watches',
    bgImage: '/static_photos/photo_slider_1.jpg'
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
    }, 9000);
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