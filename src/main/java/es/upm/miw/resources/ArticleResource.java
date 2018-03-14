package es.upm.miw.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.upm.miw.controllers.ArticleController;
import es.upm.miw.dtos.ArticleOutputDto;
import es.upm.miw.resources.exceptions.ArticleCodeNotFoundException;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(ArticleResource.ARTICLES)
public class ArticleResource {
    public static final String ARTICLES = "/articles";
    
    public static final String INCOMPLETES = "/incompletos";

    public static final String CODE_ID = "/{code}";

    @Autowired
    private ArticleController articleController;

    @GetMapping(value = CODE_ID)
    public ArticleOutputDto readArticle(@PathVariable String code) throws ArticleCodeNotFoundException {
        return this.articleController.readArticle(code).orElseThrow(() -> new ArticleCodeNotFoundException(code));
    }
    
	@RequestMapping(method = RequestMethod.POST)
	public ArticleOutputDto postArticle(@RequestBody ArticleOutputDto articleOuputDto) {
		return this.articleController.postFastArticle(articleOuputDto);

	}
	
	@RequestMapping(method = RequestMethod.GET)
    public List<ArticleOutputDto> readAllArticles() {
        return this.articleController.readAll();
    }
	
    @GetMapping(value = INCOMPLETES)
    public List<ArticleOutputDto> readAllArticlesIncompletes(){
        return this.articleController.readAllIncompletes();
    }
}
