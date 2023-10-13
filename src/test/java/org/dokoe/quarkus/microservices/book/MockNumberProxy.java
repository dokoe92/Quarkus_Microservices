package org.dokoe.quarkus.microservices.book;

import io.quarkus.test.Mock;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RestClient
@Mock
public class MockNumberProxy implements NumberProxy{

    @Override
    public IsbnThirteen generateNumbers() {
        IsbnThirteen isbnThirteen = new IsbnThirteen();
        isbnThirteen.isbn13 = "13-mock";
        return isbnThirteen;
    }
}


