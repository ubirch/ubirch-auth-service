package com.ubirch.auth.client.rest

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.client.rest.config.AuthClientRestConfig
import com.ubirch.auth.model.UserInfo
import com.ubirch.util.deepCheck.model.DeepCheckResponse
import com.ubirch.util.deepCheck.util.DeepCheckResponseUtil
import com.ubirch.util.json.MyJsonProtocol
import com.ubirch.util.model.JsonResponse

import org.json4s.native.Serialization.read

import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, StatusCode, StatusCodes}
import akka.stream.Materializer
import akka.util.ByteString

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2018-01-22
  */
object AuthServiceClientRest extends MyJsonProtocol
  with StrictLogging {

  def check()(implicit httpClient: HttpExt, materializer: Materializer): Future[Option[JsonResponse]] = {


    val url = AuthClientRestConfig.urlCheck
    httpClient.singleRequest(HttpRequest(uri = url)) flatMap {

      case HttpResponse(StatusCodes.OK, _, entity, _) =>

        entity.dataBytes.runFold(ByteString(""))(_ ++ _) map { body =>
          Some(read[JsonResponse](body.utf8String))
        }

      case res@HttpResponse(code, _, _, _) =>

        res.discardEntityBytes()
        Future(
          logErrorAndReturnNone(s"check() call to key-service failed: url=$url code=$code, status=${res.status}")
        )

    }

  }

  def deepCheck()(implicit httpClient: HttpExt, materializer: Materializer): Future[DeepCheckResponse] = {

    val statusCodes: Set[StatusCode] = Set(StatusCodes.OK, StatusCodes.ServiceUnavailable)

    val url = AuthClientRestConfig.urlDeepCheck
    httpClient.singleRequest(HttpRequest(uri = url)) flatMap {

      case HttpResponse(status, _, entity, _) if statusCodes.contains(status) =>

        entity.dataBytes.runFold(ByteString(""))(_ ++ _) map { body =>
          read[DeepCheckResponse](body.utf8String)
        }

      case res@HttpResponse(code, _, _, _) =>

        res.discardEntityBytes()
        val errorText = s"deepCheck() call to key-service failed: url=$url code=$code, status=${res.status}"
        logger.error(errorText)
        val deepCheckRes = DeepCheckResponse(status = false, messages = Seq(errorText))
        Future(
          DeepCheckResponseUtil.addServicePrefix("key-service", deepCheckRes)
        )

    }

  }

  def register(token: String)
              (implicit httpClient: HttpExt, materializer: Materializer): Future[Option[UserInfo]] = {

    val url = AuthClientRestConfig.urlRegister
    val bearerToken = Authorization(OAuth2BearerToken(token))
    val req = HttpRequest(
      method = HttpMethods.POST,
      uri = url,
      headers = bearerToken :: Nil
    )
    httpClient.singleRequest(req) flatMap {

      case HttpResponse(StatusCodes.OK, _, entity, _) =>

        entity.dataBytes.runFold(ByteString(""))(_ ++ _) map { body =>
          Some(read[UserInfo](body.utf8String))
        }

      case res@HttpResponse(code, _, _, _) =>

        res.discardEntityBytes()
        Future(
          logErrorAndReturnNone(s"register() call to auth-service failed: url=$url code=$code, status=${res.status}")
        )

    }

  }

  private def logErrorAndReturnNone[T](errorMsg: String,
                                       t: Option[Throwable] = None
                                      ): Option[T] = {
    t match {
      case None => logger.error(errorMsg)
      case Some(someThrowable: Throwable) => logger.error(errorMsg, someThrowable)
    }

    None

  }

}
