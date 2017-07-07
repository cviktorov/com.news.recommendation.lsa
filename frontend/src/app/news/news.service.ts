import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/toPromise';

import { Article } from "app/domain/article";

@Injectable()
export class NewsService {

    documentsCacheLSA: Article[];

    documentsCacheWV: Article[];

    detailCurrentAlgo = "LSA";

    lastSearch: string;

    constructor(private http: Http) { }

    getDocumentsLSA(query: string): Promise<Article[]> {
        return this.http.post('rs/news/search', query)
            .toPromise()
            .then(res => {
                this.documentsCacheLSA = res.json() as Article[];
                this.lastSearch = query;
                return this.documentsCacheLSA;
            })
            .catch(err => alert(err));
    }

    getDocumentByIdLSA(id: string): Promise<Article> {
        return Promise.resolve(this.documentsCacheLSA.find(doc => doc.id == id));
    }

    getSuggestionsLSA(document: Article): Promise<Article[]> {
        return this.http.post('rs/news/suggestions', document)
            .toPromise()
            .then(res => res.json() as Article[])
            .catch(err => alert(err));
    }

    getDocumentsWV(query: string): Promise<Article[]> {
        return this.http.post('rs/news/searchwv', query)
            .toPromise()
            .then(res => {
                this.documentsCacheWV = res.json() as Article[];
                this.lastSearch = query;
                return this.documentsCacheWV;
            })
            .catch(err => alert(err));
    }

    getDocumentByIdWV(id: string): Promise<Article> {
        return Promise.resolve(this.documentsCacheWV.find(doc => doc.id == id));
    }

    getSuggestionsWV(document: Article): Promise<Article[]> {
        return this.http.post('rs/news/suggestionswv', document)
            .toPromise()
            .then(res => res.json() as Article[])
            .catch(err => alert(err));
    }
}