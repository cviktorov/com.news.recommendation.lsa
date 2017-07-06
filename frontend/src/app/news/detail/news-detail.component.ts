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
        this.route.paramMap
            .switchMap((params: ParamMap) => this.newsService.getDocumentById(params.get('id')))
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
        this.newsService.getSuggestions(this.document)
                    .then(res => this.suggestions = res);
    }
}