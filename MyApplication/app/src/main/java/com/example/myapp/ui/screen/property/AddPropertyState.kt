package com.example.myapp.ui.screen.property

// AddPropertyState.kt
data class AddPropertyState(
    val title: String = "",
    val price: String = "",
    val location: String = "",
    val beds: Int = 0,
    val baths: Int = 0,
    val area: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccessfull: Boolean = false
)

sealed class AddPropertyEvent{
    data class onTitleChange(val title: String) : AddPropertyEvent()
    data class onPriceChange(val price: String) : AddPropertyEvent()
    data class onLocationChange(val location: String) : AddPropertyEvent()
    data class onBedsIncrease(val beds: Int) : AddPropertyEvent()
    data class onBedsDecrease(val beds: Int) : AddPropertyEvent()
    data class onBathsIncrease(val baths: Int) : AddPropertyEvent()
    data class onBathsDecrease(val baths: Int) : AddPropertyEvent()
    data class onAreaChange(val area: String) : AddPropertyEvent()
    data class onDescriptionChange(val description: String) : AddPropertyEvent()
    object OnSuccessHandled : AddPropertyEvent()
    object onSubmitClick : AddPropertyEvent()
    object onClearClick : AddPropertyEvent()

}

