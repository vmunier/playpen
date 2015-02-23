package playpen.controllers

import play.api.Logger
import play.api.mvc.{Action, Controller}
import playpen.CorsParams
import play.api.Play.current

/*
 * Usage example in the routes file:
 * OPTIONS /myservice/find/:id/with/:slug     playpen.controllers.Cors.authorize(methods: Seq[String]=Seq("PUT", "DELETE"), id, slug)
 */
object Cors extends Controller {

  lazy val allowedOrigins = current.configuration.getStringSeq("cors.allowed.origins").getOrElse(Seq("beamly.com", "zeebox.com"))

  // `params` is needed because Play checks that all the path parameters are used in the routes file.
  def authorize(methods: Seq[String], params: String*) = Action { request =>
    val headers = request match {
      case CorsParams(params) if allowedOrigins.exists(params.origin.endsWith(_)) =>
        Seq(
          "Access-Control-Allow-Origin" -> params.origin,
          "Access-Control-Allow-Methods" -> ("OPTIONS" +: methods).toSet.mkString(","),
          "Access-Control-Allow-Headers" -> params.headers.mkString(","))
      case _ => Seq()
    }

    Ok("").withHeaders(headers: _*)
  }
}
