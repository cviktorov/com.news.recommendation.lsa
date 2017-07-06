import { NewsRecommendationFEPage } from './app.po';

describe('news-recommendation-fe App', () => {
  let page: NewsRecommendationFEPage;

  beforeEach(() => {
    page = new NewsRecommendationFEPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
