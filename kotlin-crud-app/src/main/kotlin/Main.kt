package org.example

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.runBlocking
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val database = getDatabase()

    runBlocking {
        val collection = database.getCollection<RestaurantInfo>(collectionName = "restaurants")

        /** Listando as collection que já existem no banco sample_restaurants
        database.listCollectionNames().collect {
            println(it)
        }
         */

        /** Metodo que adiciona restaurantes
            addRestaurant(database)
        */

        /** consulta os Restaurantes
            readRestaurant(collection)
        */

        /** Atualizando um Restaurante
            updateRestaurant(collection, "MongoDB")
        */

        /** Removendo um item
         *
         */

        deleteRestaurant(collection, "Riveira Caterer")
    }
}

fun getDatabase(): MongoDatabase {
    val client = MongoClient.create(connectionString = System.getenv("MONGO_URI"))
    return client.getDatabase(databaseName = "sample_restaurants")
}

data class RestaurantInfo(
    @BsonId
    val id: ObjectId,
    val name: String,
    val cuisine: String,
    val borough: String,
    @BsonProperty("restaurant_id")
    val restaurantId: String
)

suspend fun addRestaurant(database: MongoDatabase){

    val info = RestaurantInfo(
        id = ObjectId(),
        name = "DiPerone",
        cuisine = "Italia",
        borough = "Milão",
        restaurantId = "randomId"
    )

    val infoB = RestaurantInfo(
        id = ObjectId(),
        name = "Rei do Aparmegiana",
        cuisine = "Brasil",
        borough = "São Paulo",
        restaurantId = "randomId"
    )

    val infoRestaurants = listOf<RestaurantInfo>(info, infoB)

    println("Lista de restaurantes a serem inseridos: $infoRestaurants")


    val collection = database.getCollection<RestaurantInfo>(collectionName = "restaurants")

    infoRestaurants.forEach { i ->
        collection.insertOne(i).also {
            println("Inserted id: ${it.insertedId}")
        }
    }

}

suspend fun readRestaurant(collection: MongoCollection<RestaurantInfo>){
        val query = Filters.or(
            listOf(
                Filters.eq("restaurants_id", "randomId"),
                Filters.eq(RestaurantInfo::borough.name, "São Paulo")
            )
        )
        collection.find<RestaurantInfo>(filter = query).limit(5).collect{
            println(it)
        }
    }

suspend fun updateRestaurant(collection: MongoCollection<RestaurantInfo>, atualizar: String ){
    val query = Filters.eq(RestaurantInfo::name.name, "Mongo")
    val updateSet = Updates.set(RestaurantInfo::name.name, atualizar)

    collection.updateOne(filter = query, update = updateSet).also {
        println("Match docs ${it.matchedCount} and update docs ${it.modifiedCount}")
     }
}

suspend fun deleteRestaurant(collection: MongoCollection<RestaurantInfo>, rvRestaurant: String){
    val query = Filters.eq(RestaurantInfo::name.name, rvRestaurant)
    collection.deleteOne(query).also{
        println("O item: ${it.deletedCount} foi removido com sucesso!")
    }

}