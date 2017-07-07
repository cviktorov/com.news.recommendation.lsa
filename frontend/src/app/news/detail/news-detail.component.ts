import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { Article } from "app/domain/article";
import { NewsService } from "app/news/news.service";
import 'rxjs/add/operator/switchMap';


@Component({
    selector: 'news-detail',
    templateUrl: 'news-detail.component.html'
})

export class NewsDetailComponent implements OnInit {

    document: Article;

    suggestions: Article[];

    constructor(
        private newsService: NewsService,
        private router: Router,
        private route: ActivatedRoute
        ) { }

    ngOnInit() {
        if (!this.newsService.documentsCacheLSA || !this.newsService.documentsCacheWV) {
            this.router.navigate(['news/search']);
            return;
        }
        this.route.paramMap
            .switchMap((params: ParamMap) => {
                const id = params.get('id');
                if (this.newsService.detailCurrentAlgo === 'LSA') {
                    return this.newsService.getDocumentByIdLSA(id);
                } else {
                    return this.newsService.getDocumentByIdWV(id);
                }
            })
            .subscribe(doc => {
                this.document = doc;
                this.getSuggestions();
            });
    }

    toSearch() {
        this.router.navigate(['search'], { relativeTo: this.route });
    }

    onDocSelect(id: string) {
        this.document = this.suggestions.find(doc => doc.id == id);
        this.getSuggestions();
    }

    getSuggestions() {
        if (this.newsService.detailCurrentAlgo === 'LSA') {
            this.newsService.getSuggestionsLSA(this.document)
                    .then(res => this.suggestions = res);
        } else {
            this.newsService.getSuggestionsWV(this.document)
                    .then(res => this.suggestions = res);
        }
    }
}