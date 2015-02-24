package playpen.cors.filters

import play.api.mvc.{EssentialFilter, EssentialAction, RequestHeader}
import playpen.cors.CorsHeaders
import scala.concurrent.ExecutionContext.Implicits.global

object CorsFilter extends EssentialFilter {
  def apply(next: EssentialAction) = new EssentialAction {
    def apply(requestHeader: RequestHeader) = {
      next(requestHeader).map { result =>
        result.withHeaders(CorsHeaders.headers(requestHeader): _*)
      }
    }
  }
}
