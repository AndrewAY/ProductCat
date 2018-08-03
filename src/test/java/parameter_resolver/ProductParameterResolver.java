package parameter_resolver;

import com.andrew.productcatalogue2.entity.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class ProductParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext
            , final ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return Objects.equals(parameter.getParameterizedType().getTypeName()
                , "java.util.Map<java.lang.String, com.andrew.productcatalogue2.entity.Product>");
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext
            , final ExtensionContext extensionContext) throws ParameterResolutionException {
        ExtensionContext.Store store = extensionContext
                .getStore(ExtensionContext.Namespace.create(Product.class));
        return store.getOrComputeIfAbsent("products", k -> getProducts());
    }


    /* 20 products(10 movies and 10 books) will be returned*/
    private Map<String, Product> getProducts() {

        Map<String, Product> products = new HashMap<>();

        MovieFormat movieFormat = new MovieFormat(1,"DVD");

        List<Language> languages = new ArrayList<>();
        Language language = new Language(1,"English");
        languages.add(language);

        for(long i=1; i<=10; i++) {
            Movie movie = new Movie();
            movie.setProductId(i);
            movie.setPrice(BigDecimal.valueOf(i));
            movie.setProductType(Product.ProductType.MOVIE);
            movie.setTitle("Movie" + i);
            movie.setImageName(i + ".jpg");
            movie.setDescription("Some description");
            // create and add movieFormat
            movie.setFormat(movieFormat);
            // create and add languages
            movie.setLanguages(languages);


            String key = "movie" + i;
            products.put(key, movie);
        }


        BookFormat bookFormat = new BookFormat(1, "Paperback");
        for(long i=11; i<=20; i++) {
            Book book = new Book();
            book.setProductId(i);
            book.setPrice(BigDecimal.valueOf(i));
            book.setProductType(Product.ProductType.BOOK);
            book.setTitle("Book" + i);
            book.setImageName(i + ".jpg");
            book.setDescription("Some description");
            // create and add movieFormat
            book.setFormat(bookFormat);
            // create and add languages
            book.setLanguage(language);


            String key = "book" + i;
            products.put(key, book);
        }

        return products;
    }




}
