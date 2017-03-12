package stackoverflow

import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health

class UrlHealthIndicator extends AbstractHealthIndicator {

    private final String url

    private final int timeout

    UrlHealthIndicator(final String url, final int timeout = 10 * 1000) {
        this.url = url
        this.timeout = timeout
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        final HttpURLConnection urlConnection =
                (HttpURLConnection) url.toURL().openConnection()

        final int responseCode =
                urlConnection.with {
                    requestMethod = 'HEAD'
                    readTimeout = timeout
                    connectTimeout = timeout
                    connect()
                    responseCode
                }

        // If code in 200 to 399 range everything is fine.
        responseCode == 200 ?
                builder.up() :
                builder.down(new Exception("Invalid responseCode '${responseCode}' checking '${url}'."))
    }
}