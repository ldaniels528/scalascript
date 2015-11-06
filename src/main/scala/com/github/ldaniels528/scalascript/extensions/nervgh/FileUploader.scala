package com.github.ldaniels528.scalascript.extensions.nervgh

import scala.scalajs.js

/**
  * Angular File Uploader (nervgh/angular-js-upload)
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploader extends js.Object {

  var filters: js.Array[FileUploadFilter] = js.native

  var onWhenAddingFileFailed: js.Function3[FileUploadItem, FileUploadFilter, FileUploadOptions, Unit] = js.native //(item /*{File|FileLikeObject}*/, filter, options)

  var onAfterAddingFile: js.Function1[FileUploadItem, Unit] = js.native //(fileItem)

  var onAfterAddingAll: js.Function1[js.Array[FileUploadItem], Unit] = js.native //(addedFileItems)

  var onBeforeUploadItem: js.Function1[FileUploadItem, Unit] = js.native //(item)

  var onProgressItem: js.Function2[FileUploadItem, FileUploadProgress, Unit] = js.native //(fileItem, progress)

  var onProgressAll: js.Function1[FileUploadProgress, Unit] = js.native //(progress)

  var onSuccessItem: js.Function4[FileUploadItem, FileUploadResponse, js.Any, FileUploadHeaders, Unit] = js.native //(fileItem, response, status, headers)

  var onErrorItem: js.Function4[FileUploadItem, FileUploadResponse, js.Any, FileUploadHeaders, Unit] = js.native //(fileItem, response, status, headers)

  var onCancelItem: js.Function4[FileUploadItem, FileUploadResponse, js.Any, FileUploadHeaders, Unit] = js.native //(fileItem, response, status, headers)

  var onCompleteItem: js.Function4[FileUploadItem, FileUploadResponse, js.Any, FileUploadHeaders, Unit] = js.native //(fileItem, response, status, headers)

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
  * @example {url: "/api/post/upload", alias: "file", headers: Object, formData: Array[0], removeAfterUpload: false…}
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploadItem extends js.Object {
  var url: String
  var alias: String
  var headers: js.Object
  var formData: js.Array[js.Object]
  var removeAfterUpload: Boolean
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

/**
  * File Upload Headers
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploadHeaders extends js.Object {

}

/**
  * File Upload Options
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploadOptions extends js.Object {

}

/**
  * File Upload Progress
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploadProgress extends js.Object {

}

/**
  * File Upload Response
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploadResponse extends js.Object {

}