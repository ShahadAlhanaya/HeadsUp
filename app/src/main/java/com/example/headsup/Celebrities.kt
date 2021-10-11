package com.example.headsup

import com.google.gson.annotations.SerializedName


class Celebrities() {
    var data: List<Celebrity>? = null

    class Celebrity {

        @SerializedName("pk")// primary key
        var pk: Int? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("taboo1")
        var taboo1: String? = null

        @SerializedName("taboo2")
        var taboo2: String? = null

        @SerializedName("taboo3")
        var taboo3: String? = null


        constructor(name: String?, taboo1: String?, taboo2: String?, taboo3: String?) {
            this.name = name
            this.taboo1 = taboo1
            this.taboo2 = taboo2
            this.taboo3 = taboo3
//            this.pk = pk
        }
    }
}


