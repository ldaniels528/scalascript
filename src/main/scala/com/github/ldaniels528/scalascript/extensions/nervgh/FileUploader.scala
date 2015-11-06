package com.github.ldaniels528.scalascript.extensions.nervgh

import scala.scalajs.js

/**
  * nervgh/angular-js-upload
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploader extends js.Object {

  var filters: js.Array[FileUploadFilter] = js.native

  var onWhenAddingFileFailed: js.Function3[FileUploadItem, FileUploadFilter, js.Object, Unit] = js.native //(item /*{File|FileLikeObject}*/, filter, options)

  var onAfterAddingFile: js.Function1[FileUploadItem, Unit] = js.native //(fileItem)

  var onAfterAddingAll: js.Function1[js.Array[FileUploadItem], Unit] = js.native //(addedFileItems)

  var onBeforeUploadItem: js.Function1[FileUploadItem, Unit] = js.native //(item)

  var onProgressItem: js.Function2[FileUploadItem, js.Object, Unit] = js.native //(fileItem, progress)

  var onProgressAll: js.Function1[js.Object, Unit] = js.native //(progress)

  var onSuccessItem: js.Function4[FileUploadItem, js.Object, js.Any, js.Object, Unit] = js.native //(fileItem, response, status, headers)

  var onErrorItem: js.Function4[FileUploadItem, js.Object, js.Any, js.Object, Unit] = js.native //(fileItem, response, status, headers)

  var onCancelItem: js.Function4[FileUploadItem, js.Object, js.Any, js.Object, Unit] = js.native //(fileItem, response, status, headers)

  var onCompleteItem: js.Function4[FileUploadItem, js.Object, js.Any, js.Object, Unit] = js.native //(fileItem, response, status, headers)

  var onCompleteAll: js.Function0[Unit] = js.native

}

/**
  * File Uploader Companion
  * @author lawrence.daniels@gmail.com
  */
object FileUploader {

  def apply(jsClass: js.Any, config: FileUploaderConfig) = {
    val inst = js.Dynamic.newInstance(jsClass.asInstanceOf[js.Dynamic])(config)
    inst.asInstanceOf[FileUploader]
  }
}

/**
  * File Upload Item
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploadItem extends js.Object {
  var url: String
}

/**
  * File Upload Filter
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploadFilter extends js.Object {
  var name: String
  var fn: js.Function
}