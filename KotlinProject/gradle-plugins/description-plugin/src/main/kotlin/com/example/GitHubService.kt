import okhttp3.OkHttpClient
import okhttp3.Request
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Json

data class CommitInfo(
    @Json(name = "sha") val sha: String,
    @Json(name = "commit") val commit: CommitDetails
)

data class CommitDetails(
    val author: CommitAuthor,
    val message: String
)

data class CommitAuthor(
    val name: String,
    val email: String,
    val date: String
)

fun getLatestCommitInfo(owner: String, repo: String, branch: String = "main") {
    val url = "https://api.github.com/repos/$owner/$repo/commits/$branch"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            println("Failed to fetch latest commit: ${response.code}")
            return
        }

        val body = response.body?.string() ?: ""
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())  // Use this for Kotlin data classes
            .build()
        val jsonAdapter = moshi.adapter(CommitInfo::class.java)
        val commitInfo = jsonAdapter.fromJson(body)

        if (commitInfo != null) {
            println("Latest Commit SHA: ${commitInfo.sha}")
            println("Author: ${commitInfo.commit.author.name} <${commitInfo.commit.author.email}>")
            println("Date: ${commitInfo.commit.author.date}")
            println("Message: ${commitInfo.commit.message}")
        } else {
            println("Failed to parse commit information.")
        }
    }
}
