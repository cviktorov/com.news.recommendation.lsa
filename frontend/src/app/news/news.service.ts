import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/toPromise';
import { Article } from "app/domain/article";

@Injectable()
export class NewsService {

    mockDocuments = [
        new Article('1', 'Sample text'),
        new Article('2', 'Document content'),
        new Article('3', 'Maiko mila news'),
        new Article('4', 'Document content'),
        new Article('5', 'Document content'),
        new Article('6', 'Document content'),
        new Article('7', 'Document content'),
        new Article('8', 'Document content'),
        new Article('9', 'Document content')
    ]

    constructor(private http: Http) { }

    getDocuments(query: string): Promise<Article[]> {
        return Promise.resolve(this.mockDocuments.filter(doc => doc.content.includes(query)));
    }

    getDocumentById(id: string): Promise<Article> {
        return Promise.resolve(this.mockDocuments.find(doc => doc.id == id));
    }
}