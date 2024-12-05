package com.tech.indiaekartshoppinguser.data.repositoryImpl

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.common.ADD_TO_CART
import com.tech.indiaekartshoppinguser.common.ADD_TO_CART_BY_USER
import com.tech.indiaekartshoppinguser.common.CATEGORY
import com.tech.indiaekartshoppinguser.common.FAVOURITE
import com.tech.indiaekartshoppinguser.common.FAVOURITE_BY_USER
import com.tech.indiaekartshoppinguser.common.ORDER
import com.tech.indiaekartshoppinguser.common.ORDER_DATA
import com.tech.indiaekartshoppinguser.common.OneTapSignInResult
import com.tech.indiaekartshoppinguser.common.PRODUCT
import com.tech.indiaekartshoppinguser.common.ResultState
import com.tech.indiaekartshoppinguser.common.SHIPPING_DATA
import com.tech.indiaekartshoppinguser.common.USER
import com.tech.indiaekartshoppinguser.common.USER_TOKEN
import com.tech.indiaekartshoppinguser.data.PushNotification
import com.tech.indiaekartshoppinguser.domain.models.CartModel
import com.tech.indiaekartshoppinguser.domain.models.Category
import com.tech.indiaekartshoppinguser.domain.models.OrderModel
import com.tech.indiaekartshoppinguser.domain.models.ProductModel
import com.tech.indiaekartshoppinguser.domain.models.ShippingModel
import com.tech.indiaekartshoppinguser.domain.models.UserModel
import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseMessaging: FirebaseMessaging,
    private val oneTapClient: SignInClient,
    private val pushNotification: PushNotification
) : ShoppingRepository {
    override fun getAllCategory(): Flow<ResultState<List<Category>>> = callbackFlow {

        trySend(ResultState.Loading)
        firebaseFireStore.collection(CATEGORY).get()
            .addOnSuccessListener {
                val categoryData = it.documents.mapNotNull { document ->
                    val category = document.toObject(Category::class.java)
                    category?.categoryId = document.id
                    category
                }
                trySend(ResultState.Success(categoryData))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun getAllProducts(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(PRODUCT).get()
            .addOnSuccessListener {
                val productData = it.documents.mapNotNull { document ->
                    val product = document.toObject(ProductModel::class.java)
                    product?.productId = document.id
                    product
                }

                trySend(ResultState.Success(productData))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun registerUserWithEmailPassword(userModel: UserModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseAuth.createUserWithEmailAndPassword(userModel.email, userModel.password)
                .addOnSuccessListener {
                    userModel.uuid = it.user?.uid.toString()
                    firebaseFireStore.collection(USER).document(it.user?.uid.toString())
                        .set(userModel).addOnSuccessListener {
                            trySend(ResultState.Success("User Registered Successfully"))
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it.message.toString()))
                        }
                    updateFcmToken(it.user?.uid.toString())
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun loginUserWithEmailPassword(userModel: UserModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseAuth.signInWithEmailAndPassword(userModel.email, userModel.password)
                .addOnSuccessListener {
                    trySend(ResultState.Success("User Login Successfully"))
                    updateFcmToken(firebaseAuth.currentUser?.uid.toString())
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun getProductById(productId: String): Flow<ResultState<ProductModel>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(PRODUCT).document(productId).get()
            .addOnSuccessListener { document ->
                val product = document.toObject(ProductModel::class.java)
                product?.productId = document.id
                trySend(ResultState.Success(product!!))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun getUserData(uuid: String): Flow<ResultState<UserModel>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFireStore.collection(USER).document(uuid).get().addOnSuccessListener {
            val user = it.toObject(UserModel::class.java).apply {
                this?.uuid = it.id
            }
            trySend(ResultState.Success(user!!))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun updateUserData(userModel: UserModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(USER).document(userModel.uuid).set(userModel)
            .addOnSuccessListener {
                trySend(ResultState.Success("User Data Updated Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun uploadUserImage(imageUri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseStorage.reference.child("usersImages/${firebaseAuth.currentUser?.uid}")
            .putFile(imageUri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    trySend(ResultState.Success(image.toString()))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun productInFavouritePerform(productModel: ProductModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFireStore.collection(FAVOURITE)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(
                    FAVOURITE_BY_USER
                )
                .whereEqualTo("productId", productModel.productId).get().addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        Log.d("@remove", "removeProductInFavourite: ${it.documents[0].id}")
                        firebaseFireStore.collection(FAVOURITE)
                            .document(firebaseAuth.currentUser?.uid.toString()).collection(
                                FAVOURITE_BY_USER
                            ).document(it.documents[0].id)
                            .delete()
                            .addOnSuccessListener {
                                trySend(ResultState.Success("Product Removed From Favourite"))
                                close()
                            }.addOnFailureListener {
                                trySend(ResultState.Error(it.message.toString()))
                                close()
                            }
                        return@addOnSuccessListener
                    } else {
                        firebaseFireStore.collection(FAVOURITE)
                            .document(firebaseAuth.currentUser?.uid.toString())
                            .collection(FAVOURITE_BY_USER).document()
                            .set(productModel).addOnSuccessListener {
                                trySend(ResultState.Success("Product Added In Favourite"))
                                close()
                            }.addOnFailureListener {
                                trySend(ResultState.Error(it.message.toString()))
                                close()
                            }
                    }
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                    return@addOnFailureListener
                }
            awaitClose {
                close()
            }
        }

    override fun checkProductInFavouriteOrNot(productId: String): Flow<ResultState<Boolean>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFireStore.collection(FAVOURITE)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(
                    FAVOURITE_BY_USER
                ).whereEqualTo("productId", productId).get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        trySend(ResultState.Success(true))
                    } else {
                        trySend(ResultState.Success(false))
                    }
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun getAllFavouriteProducts(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(FAVOURITE).document(firebaseAuth.currentUser?.uid.toString())
            .collection(
                FAVOURITE_BY_USER
            ).get().addOnSuccessListener {
                val productData = it.documents.mapNotNull { document ->
                    document.toObject(ProductModel::class.java)
                }
                trySend(ResultState.Success(productData))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun productInCartPerform(cartModel: CartModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFireStore.collection(ADD_TO_CART)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(
                    ADD_TO_CART_BY_USER
                )
                .whereEqualTo("productId", cartModel.productId).get().addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        firebaseFireStore.collection(ADD_TO_CART)
                            .document(firebaseAuth.currentUser?.uid.toString()).collection(
                                ADD_TO_CART_BY_USER
                            ).document(it.documents[0].id)
                            .delete()
                            .addOnSuccessListener {
                                trySend(ResultState.Success("Product Removed From Cart"))
                                close()
                            }.addOnFailureListener {
                                trySend(ResultState.Error(it.message.toString()))
                                close()
                            }
                        return@addOnSuccessListener
                    } else {
                        firebaseFireStore.collection(ADD_TO_CART)
                            .document(firebaseAuth.currentUser?.uid.toString())
                            .collection(ADD_TO_CART_BY_USER).document()
                            .set(cartModel).addOnSuccessListener {
                                trySend(ResultState.Success("Product Added In Cart"))
                                close()
                            }.addOnFailureListener {
                                trySend(ResultState.Error(it.message.toString()))
                                close()
                            }
                    }
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                    return@addOnFailureListener
                }
            awaitClose {
                close()
            }

        }

    override fun checkProductInCartOrNot(productId: String): Flow<ResultState<Boolean>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFireStore.collection(ADD_TO_CART)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(
                    ADD_TO_CART_BY_USER
                ).whereEqualTo("productId", productId).get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        trySend(ResultState.Success(true))
                    } else {
                        trySend(ResultState.Success(false))
                    }
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun getAllCartProducts(): Flow<ResultState<List<CartModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(ADD_TO_CART).document(firebaseAuth.currentUser?.uid.toString())
            .collection(
                ADD_TO_CART_BY_USER
            ).get().addOnSuccessListener {
                val cartData = it.documents.mapNotNull { document ->
                    document.toObject(CartModel::class.java)
                }
                trySend(ResultState.Success(cartData))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun initiateOneTapSignIn(
        activity: Activity,
        signInLauncher: ActivityResultLauncher<IntentSenderRequest>
    ): Flow<OneTapSignInResult> = callbackFlow {
        trySend(OneTapSignInResult.Loading)
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(activity.getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
                signInLauncher.launch(intentSenderRequest)
                trySend(OneTapSignInResult.OneTapSuccess(true))
                Log.d("GoogleOneTap", "initiateOneTapSignIn: success")
            }
            .addOnFailureListener { e ->
                trySend(OneTapSignInResult.Error(e.localizedMessage ?: "Unknown error"))
                Log.d("GoogleOneTap", e.localizedMessage ?: "Unknown error")
            }
        awaitClose {
            close()
        }
    }

    override fun firebaseWithGoogle(idToken: String): Flow<OneTapSignInResult> = callbackFlow {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userModel = UserModel(
                        firstName = firebaseAuth.currentUser?.displayName.toString().split(" ")
                            .first(),
                        lastName = firebaseAuth.currentUser?.displayName.toString().split(" ")
                            .last(),
                        email = firebaseAuth.currentUser?.email.toString(),
                        userImage = firebaseAuth.currentUser?.photoUrl.toString(),
                        uuid = firebaseAuth.currentUser?.uid.toString()
                    )
                    firebaseFireStore.collection(USER)
                        .document(firebaseAuth.currentUser?.uid.toString())
                        .set(userModel).addOnSuccessListener {
                            trySend(OneTapSignInResult.FirebaseSuccess(true))
                        }.addOnFailureListener {
                            trySend(
                                OneTapSignInResult.Error(
                                    task.exception?.localizedMessage ?: "Something went wrong."
                                )
                            )

                        }
                    updateFcmToken(firebaseAuth.currentUser?.uid.toString())
                } else {
                    trySend(
                        OneTapSignInResult.Error(
                            task.exception?.localizedMessage ?: "Authentication failed"
                        )
                    )
                }
            }
        awaitClose {
            close()
        }
    }

    override fun shippingDataSave(shippingModel: ShippingModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFireStore.collection(SHIPPING_DATA)
                .document(firebaseAuth.currentUser?.uid.toString()).set(shippingModel)
                .addOnSuccessListener {
                    trySend(ResultState.Success("Shipping Data Saved Successfully"))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun shippingDataGetThroughUID(): Flow<ResultState<ShippingModel>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(SHIPPING_DATA).document(firebaseAuth.currentUser?.uid!!).get()
            .addOnSuccessListener {
                val shippingData = it.toObject(ShippingModel::class.java)
                Log.d("@payment", "shippingDataGetThroughUID: ${shippingData?.saveForNextTime}")
                trySend(ResultState.Success(shippingData.let {
                    shippingData ?: ShippingModel()
                }))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun orderDataSave(orderList: List<OrderModel>): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            val orderMap =
                orderList.mapIndexed { index, order -> index.toString() to order }.toMap()
            firebaseFireStore.collection(ORDER_DATA)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(ORDER).document()
                .set(orderMap).addOnSuccessListener {
                    trySend(ResultState.Success("Order Successfully"))
                    pushNotification.sendNotification(
                        title = "Order Initiate",
                        message = "Order Successfully"
                    )
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun deleteProductInCart(): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val documentId = firebaseAuth.currentUser?.uid.toString()
        Log.d("@cart", "deleteProductInCart: $documentId")
        val cartDocRef = firebaseFireStore.collection(ADD_TO_CART).document(documentId)

        //Delete subcollection  first
        cartDocRef.collection(ADD_TO_CART_BY_USER).get()
            .addOnSuccessListener { subcollectionSnapshot ->
                val deleteTasks = mutableListOf<Task<Void>>()
                // Iterate over all documents in the subcollection and delete them
                for (document in subcollectionSnapshot.documents) {
                    val deleteTask = document.reference.delete()
                    deleteTasks.add(deleteTask)
                }
                // Wait for all subcollection deletions to complete
                Tasks.whenAll(deleteTasks)
                    .addOnSuccessListener {
                        //Now delete the parent document
                        cartDocRef.delete()
                            .addOnSuccessListener {
                                trySend(ResultState.Success("Document and subcollection successfully deleted!"))
                            }.addOnFailureListener {
                                trySend(ResultState.Error("Failed to delete parent document: ${it.message.toString()}"))
                            }
                    }.addOnFailureListener {
                        trySend(ResultState.Error("Failed to delete subcollection documents: ${it.message.toString()}"))
                    }

            }.addOnFailureListener {
                trySend(ResultState.Error("Failed to fetch subcollection documents: ${it.message.toString()}"))
            }
        awaitClose {
            close()
        }
    }

    override fun getAllOrderData(): Flow<ResultState<List<OrderModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFireStore.collection(ORDER_DATA).document(firebaseAuth.currentUser?.uid.toString())
            .collection(ORDER).get().addOnSuccessListener { querySnapshot ->
                val allOrders = mutableListOf<OrderModel>()

                querySnapshot.documents.forEach { document ->
                    // Iterate over all fields in the document
                    val maps = document.data // Get all the fields as a Map<String, Any>
                    maps?.forEach { (_, value) ->
                        val orderMap = value as? Map<*, *> // Cast each value to Map
                        orderMap?.let {
                           val order =  OrderModel(
                                city = it["city"] as? String ?: "",
                                color = it["color"] as? String ?: "",
                                countryRegion = it["countryRegion"] as? String ?: "",
                                date = it["date"] as? Long ?: 0,
                                firstName = it["firstName"] as? String ?: "",
                                lastName = it["lastName"] as? String ?: "",
                                mobileNo = it["mobileNo"] as? String ?: "",
                                postalCode = it["postalCode"] as? String ?: "",
                                productCategory = it["productCategory"] as? String ?: "",
                                productId = it["productId"] as? String ?: "",
                                productDescription = it["productDescription"] as? String ?: "",
                                productFinalPrice = it["productFinalPrice"] as? String ?: "",
                                productImageUrl = it["productImageUrl"] as? String ?: "",
                                productPrice = it["productPrice"] as? String ?: "",
                                productName = it["productName"] as? String ?: "",
                                productQty = it["productQty"] as? String ?: "",
                                size = it["size"] as? String ?: "",
                                totalPrice = it["totalPrice"] as? String ?: "",
                                transactionId = it["transactionId"] as? String ?: "",
                                transactionMethod = it["transactionMethod"] as? String ?: "",
                                userAddress = it["userAddress"] as? String ?: "",
                                userEmail = it["userEmail"] as? String ?: "",
                                orderId = it["orderId"] as? String ?: "",
                            )
                            allOrders.add(order)
                        }
                    }
                }
                if (allOrders.isEmpty()) {
                    trySend(ResultState.Error("No orders found"))
                } else {
                    trySend(ResultState.Success(allOrders))
                }

                Log.d("@order", "getAllOrderData: ${allOrders.get(0).productName}")
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    private fun updateFcmToken(userId: String) {
        firebaseMessaging.token.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result
                firebaseFireStore.collection(USER_TOKEN).document(userId)
                    .set(mapOf("token" to token))
            }
        }
    }

}