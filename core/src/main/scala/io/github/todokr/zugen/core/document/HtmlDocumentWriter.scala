package io.github.todokr.zugen.core.document

import java.nio.charset.StandardCharsets
import java.nio.file.Files

import io.github.todokr.zugen.core.Config
import io.github.todokr.zugen.core.Config.GenDocumentType.{GenDomainObjectTable, GenDomainRelationDiagram}
import io.github.todokr.zugen.core.document.Document.{DomainObjectTableDoc, DomainRelationDiagramDoc}
import io.github.todokr.zugen.core.document.DocumentWriter.GeneratedDocumentPath
import io.github.todokr.zugen.core.models.DocumentMaterial

/**
  * ドキュメントデータをHTMLとして書き出す
  */
object HtmlDocumentWriter extends DocumentWriter {

  override def write(material: DocumentMaterial, config: Config): Seq[GeneratedDocumentPath] = {
    val htmlWithName = config.documentsToGenerate.genDocTypes.map {
      case GenDomainObjectTable =>
        val doc = DomainObjectTableDoc.of(material, config)
        doc.docName -> views.html.domainobject.DomainObjectTable(doc).body
      case GenDomainRelationDiagram =>
        val doc = DomainRelationDiagramDoc.of(material, config)
        doc.docName -> views.html.domainobject.DomainRelationDiagram(doc).body
    }

    htmlWithName.map {
      case (docName, html) =>
        val filePath = config.documentPath.value.resolve(s"$docName.html")
        Files.write(filePath, html.getBytes(StandardCharsets.UTF_8))
        GeneratedDocumentPath(filePath)
    }
  }
}