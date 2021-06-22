package com.example.shopapp.Model

import com.example.shopapp.Resource
import com.example.shopapp.Util.Util
import org.jsoup.Jsoup
import java.lang.Exception

class DataRequest {

    fun searchProduct(search: String, page: Int? = null, filtering: String? = null): Resource<List<Product>> {
        val url = "${Util.BASE_URL}$search/?page=${page ?: 1}&${filtering ?: ""}"
        val doc = Jsoup.connect(url).get()
        val priceData = doc.select("div.product-list").select("div.product-list--list-page")
        val data = doc.select("div.product-list__content")
        val image = doc.select("div.slider-img")
        var size = data.size
        if (size > 16) {
            size = 16
        }

        var price: String?
        var title: String?
        var img: String?
        var star: String?
        var link: String?
        val list = ArrayList<Product>()

        for (i in 0..size - 1) {
            price = priceData.select("div.product-list__content")
                .select("div.product-list__cost")
                .select("span.product-list__price")
                .eq(i)
                .text()
            title = data.select("a.product-list__link")
                .select("div.product-list__product-name")
                .eq(i)
                .text()
            img = image.select("img.lazyimg")
                .eq(i)
                .attr("data-src")
            star = data.select("div.wrapper-star")
                .select("div.rank-star")
                .select("span.score")
                .eq(i)
                .attr("style")
                .replace("\\s".toRegex(), "")
            link = "https://www.vatanbilgisayar.com${
                data.select("a.product-list__link")
                    .eq(i)
                    .attr("href")
            }"
            if (title.isNotEmpty()) {
                list.add(Product(title, img, price, link, star = star))
            }
        }

        return Resource.Success(list.toList())
    }

    fun getOpportunityProducts(): Resource<List<Product>> {
        val url = "https://www.vatanbilgisayar.com/"
        val doc = Jsoup.connect(url).get()
        val data = doc.select("div.col-lg-3").select("div.col-md-3").select("div.col-sm-6")
        val size = data.size

        var price: String?
        var title: String?
        var img: String?
        var link: String?
        val list = ArrayList<Product>()

        for (i in 0..size - 1) {
            price = data.select("div.product-list__content")
                .select("div.product-list__cost")
                .select("span.product-list__price")
                .eq(i)
                .text()
            title = data.select("div.product-list__content")
                .select("a.product-list__link")
                .select("div.product-list__product-name")
                .eq(i)
                .text()
            img = data.select("div.slider-img")
                .select("img.lazyimg")
                .eq(i)
                .attr("data-src")
            link = "https://www.vatanbilgisayar.com${
                data.select("div.product-list__content")
                    .select("a.product-list__link")
                    .eq(i)
                    .attr("href")
            }"

            list.add(Product(title, img, price, link))
        }

        return Resource.Success(list.toList())
    }

    fun getCartProducts(
        links: ArrayList<String>,
        counts: ArrayList<Int>
    ): Resource<List<Product>> {
        val resource = getProducts(links)
        if (resource is Resource.Success) {
            resource.data?.let {
                for (i in 0..it.size - 1) {
                    it.get(i).count = counts.get(i)
                }
            }
        }
        return resource
    }

    fun getProducts(links: ArrayList<String>): Resource<List<Product>> {
        return try {
            val list = ArrayList<Product>()
            links.forEach { url ->
                val doc = Jsoup.connect(url).get()
                val data = doc
                    .select("div.col-xs-12")
                    .select("div.col-sm-12")
                    .select("div.col-md-12")
                    .select("div.col-lg-12")
                    .select("div.product-detail")

                val title = data.select("div.product-list__content")
                    .select("div.product-detail-big-price")
                    .select("h1.product-list__product-name")
                    .text()
                val price = data.select("div.product-list__content")
                    .select("div.product-detail-big-price")
                    .select("div.product-list__cost")
                    .select("div.product-list__description")
                    .select("span.product-list__price")
                    .text()
                val image = doc.select("img.swiper-lazy")
                    .select("img.img-responsive")
                    .select("img.wrapper-main-slider__image")
                    .eq(0)
                    .attr("data-src")
                val star = doc.select("div.clearfix")
                    .select("div.prod-code-rank")
                    .select("div.wrapper-star")
                    .select("div.rank-star")
                    .select("span.score")
                    .attr("style")
                    .replace("\\s".toRegex(), "")


                list.add(Product(title, image, price, url, star = star))
            }
            Resource.Success(list.toList())

        } catch (e: Exception) {
            Resource.Message(e.localizedMessage ?: "Error!")
        }
    }

    fun getProductDetail(url: String): Resource<Product> {
        val doc = Jsoup.connect(url).get()
        val data = doc
            .select("div.col-xs-12")
            .select("div.col-sm-12")
            .select("div.col-md-12")
            .select("div.col-lg-12")
            .select("div.product-detail")

        val title = data.select("div.product-list__content")
            .select("div.product-detail-big-price")
            .select("h1.product-list__product-name")
            .text()
        val price = data.select("div.product-list__content")
            .select("div.product-detail-big-price")
            .select("div.product-list__cost")
            .select("div.product-list__description")
            .select("span.product-list__price")
            .text()
        val image = doc.select("img.swiper-lazy")
            .select("img.img-responsive")
            .select("img.wrapper-main-slider__image")
            .eq(0)
            .attr("data-src")
        val star = doc.select("div.clearfix")
            .select("div.prod-code-rank")
            .select("div.wrapper-star")
            .select("div.rank-star")
            .select("span.score")
            .attr("style")
            .replace("\\s".toRegex(), "")

        val featuresSize = doc.select("ul.pdetail-property-list")
            .select("li")
            .size
        val features = ArrayList<String>()
        for (i in 0..featuresSize - 1) {
            val feature = doc.select("ul.pdetail-property-list")
                .select("li")
                .eq(i)
                .select("span")
                .text()
            features.add(feature)
        }

        val imagesSize = doc.select("div.swiper-slide")
            .size
        val images = ArrayList<String>()
        for (i in 0..imagesSize - 1) {
            val img = doc.select("div.swiper-slide")
                .eq(i)
                .select("a")
                .attr("href")
            images.add(img)
        }

        val category = doc.select("input[id=visilabs-categoryId]")
            .attr("value")

        return Resource.Success(
            Product(
                title,
                image,
                price,
                url,
                star = star,
                features = features,
                images = images,
                category = category
            )
        )
    }

}