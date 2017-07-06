import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/toPromise';

import { Article } from "app/domain/article";

@Injectable()
export class NewsService {

    documentsCache: Article[];

    lastSearch: string;

    constructor(private http: Http) { }

    getDocuments(query: string): Promise<Article[]> {
        return this.http.post('rs/news/search', query)
            .toPromise()
            .then(res => {
                this.documentsCache = res.json() as Article[];
                this.lastSearch = query;
                return this.documentsCache;
            })
            .catch(err => alert(err));
    }

    getDocumentById(id: string): Promise<Article> {
        return Promise.resolve(this.documentsCache.find(doc => doc.id == id));
    }

    getSuggestions(document: Article): Promise<Article[]> {
        return this.http.post('rs/news/suggestions', document)
            .toPromise()
            .then(res => res.json() as Article[])
            .catch(err => alert(err));
    }
}