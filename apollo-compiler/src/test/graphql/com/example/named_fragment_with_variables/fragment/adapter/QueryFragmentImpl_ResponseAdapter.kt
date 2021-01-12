// AUTO-GENERATED FILE. DO NOT MODIFY.
//
// This class was automatically generated by Apollo GraphQL plugin from the GraphQL queries it found.
// It should not be modified by hand.
//
package com.example.named_fragment_with_variables.fragment.adapter

import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.api.internal.ResponseAdapter
import com.apollographql.apollo.api.internal.ResponseReader
import com.apollographql.apollo.api.internal.ResponseWriter
import com.example.named_fragment_with_variables.fragment.QueryFragmentImpl
import kotlin.Array
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

@Suppress("NAME_SHADOWING", "UNUSED_ANONYMOUS_PARAMETER", "LocalVariableName",
    "RemoveExplicitTypeArguments", "NestedLambdaShadowedImplicitParameter", "PropertyName",
    "RemoveRedundantQualifierName")
object QueryFragmentImpl_ResponseAdapter : ResponseAdapter<QueryFragmentImpl.Data> {
  private val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
    ResponseField.forString("__typename", "__typename", null, false, null),
    ResponseField.forObject("organization", "organization", mapOf<String, Any?>(
      "id" to mapOf<String, Any?>(
        "kind" to "Variable",
        "variableName" to "organizationId")), true, null)
  )

  override fun fromResponse(reader: ResponseReader, __typename: String?): QueryFragmentImpl.Data {
    return Data.fromResponse(reader, __typename)
  }

  override fun toResponse(writer: ResponseWriter, value: QueryFragmentImpl.Data) {
    Data.toResponse(writer, value)
  }

  object Data : ResponseAdapter<QueryFragmentImpl.Data> {
    private val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
      ResponseField.forString("__typename", "__typename", null, false, null),
      ResponseField.forObject("organization", "organization", mapOf<String, Any?>(
        "id" to mapOf<String, Any?>(
          "kind" to "Variable",
          "variableName" to "organizationId")), true, null)
    )

    override fun fromResponse(reader: ResponseReader, __typename: String?): QueryFragmentImpl.Data {
      return reader.run {
        var __typename: String? = __typename
        var organization: QueryFragmentImpl.Data.Organization? = null
        while(true) {
          when (selectField(RESPONSE_FIELDS)) {
            0 -> __typename = readString(RESPONSE_FIELDS[0])
            1 -> organization = readObject<QueryFragmentImpl.Data.Organization>(RESPONSE_FIELDS[1]) { reader ->
              Organization.fromResponse(reader)
            }
            else -> break
          }
        }
        QueryFragmentImpl.Data(
          __typename = __typename!!,
          organization = organization
        )
      }
    }

    override fun toResponse(writer: ResponseWriter, value: QueryFragmentImpl.Data) {
      writer.writeString(RESPONSE_FIELDS[0], value.__typename)
      if(value.organization == null) {
        writer.writeObject(RESPONSE_FIELDS[1], null)
      } else {
        writer.writeObject(RESPONSE_FIELDS[1]) { writer ->
          Organization.toResponse(writer, value.organization)
        }
      }
    }

    object Organization : ResponseAdapter<QueryFragmentImpl.Data.Organization> {
      private val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
        ResponseField.forString("id", "id", null, false, null),
        ResponseField.forList("user", "user", mapOf<String, Any?>(
          "query" to mapOf<String, Any?>(
            "kind" to "Variable",
            "variableName" to "query")), false, null)
      )

      override fun fromResponse(reader: ResponseReader, __typename: String?):
          QueryFragmentImpl.Data.Organization {
        return reader.run {
          var id: String? = null
          var user: List<QueryFragmentImpl.Data.Organization.User>? = null
          while(true) {
            when (selectField(RESPONSE_FIELDS)) {
              0 -> id = readString(RESPONSE_FIELDS[0])
              1 -> user = readList<QueryFragmentImpl.Data.Organization.User>(RESPONSE_FIELDS[1]) { reader ->
                reader.readObject<QueryFragmentImpl.Data.Organization.User> { reader ->
                  User.fromResponse(reader)
                }
              }?.map { it!! }
              else -> break
            }
          }
          QueryFragmentImpl.Data.Organization(
            id = id!!,
            user = user!!
          )
        }
      }

      override fun toResponse(writer: ResponseWriter, value: QueryFragmentImpl.Data.Organization) {
        writer.writeString(RESPONSE_FIELDS[0], value.id)
        writer.writeList(RESPONSE_FIELDS[1], value.user) { value, listItemWriter ->
          listItemWriter.writeObject { writer ->
            User.toResponse(writer, value)
          }
        }
      }

      object User : ResponseAdapter<QueryFragmentImpl.Data.Organization.User> {
        private val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
          ResponseField.forString("__typename", "__typename", null, false, null)
        )

        override fun fromResponse(reader: ResponseReader, __typename: String?):
            QueryFragmentImpl.Data.Organization.User {
          val typename = __typename ?: reader.readString(RESPONSE_FIELDS[0])
          return when(typename) {
            "User" -> UserUser.fromResponse(reader, typename)
            else -> OtherUser.fromResponse(reader, typename)
          }
        }

        override fun toResponse(writer: ResponseWriter,
            value: QueryFragmentImpl.Data.Organization.User) {
          when(value) {
            is QueryFragmentImpl.Data.Organization.User.UserUser -> UserUser.toResponse(writer, value)
            is QueryFragmentImpl.Data.Organization.User.OtherUser -> OtherUser.toResponse(writer, value)
          }
        }

        object UserUser : ResponseAdapter<QueryFragmentImpl.Data.Organization.User.UserUser> {
          private val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
            ResponseField.forString("__typename", "__typename", null, false, null),
            ResponseField.forString("firstName", "firstName", null, false, null),
            ResponseField.forString("lastName", "lastName", null, false, null),
            ResponseField.forString("avatar", "avatar", mapOf<String, Any?>(
              "size" to mapOf<String, Any?>(
                "kind" to "Variable",
                "variableName" to "size")), false, null)
          )

          override fun fromResponse(reader: ResponseReader, __typename: String?):
              QueryFragmentImpl.Data.Organization.User.UserUser {
            return reader.run {
              var __typename: String? = __typename
              var firstName: String? = null
              var lastName: String? = null
              var avatar: String? = null
              while(true) {
                when (selectField(RESPONSE_FIELDS)) {
                  0 -> __typename = readString(RESPONSE_FIELDS[0])
                  1 -> firstName = readString(RESPONSE_FIELDS[1])
                  2 -> lastName = readString(RESPONSE_FIELDS[2])
                  3 -> avatar = readString(RESPONSE_FIELDS[3])
                  else -> break
                }
              }
              QueryFragmentImpl.Data.Organization.User.UserUser(
                __typename = __typename!!,
                firstName = firstName!!,
                lastName = lastName!!,
                avatar = avatar!!
              )
            }
          }

          override fun toResponse(writer: ResponseWriter,
              value: QueryFragmentImpl.Data.Organization.User.UserUser) {
            writer.writeString(RESPONSE_FIELDS[0], value.__typename)
            writer.writeString(RESPONSE_FIELDS[1], value.firstName)
            writer.writeString(RESPONSE_FIELDS[2], value.lastName)
            writer.writeString(RESPONSE_FIELDS[3], value.avatar)
          }
        }

        object OtherUser : ResponseAdapter<QueryFragmentImpl.Data.Organization.User.OtherUser> {
          private val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
            ResponseField.forString("__typename", "__typename", null, false, null)
          )

          override fun fromResponse(reader: ResponseReader, __typename: String?):
              QueryFragmentImpl.Data.Organization.User.OtherUser {
            return reader.run {
              var __typename: String? = __typename
              while(true) {
                when (selectField(RESPONSE_FIELDS)) {
                  0 -> __typename = readString(RESPONSE_FIELDS[0])
                  else -> break
                }
              }
              QueryFragmentImpl.Data.Organization.User.OtherUser(
                __typename = __typename!!
              )
            }
          }

          override fun toResponse(writer: ResponseWriter,
              value: QueryFragmentImpl.Data.Organization.User.OtherUser) {
            writer.writeString(RESPONSE_FIELDS[0], value.__typename)
          }
        }
      }
    }
  }
}
