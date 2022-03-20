package api.coordinate

data class License(
    val name: String,
    val url: String,
    val distribution: String = "repo",
)

data class Developer(
    val name: String,
    val id: String,
    val email: String?,
) {
    constructor(
        nameAndId: String,
        email: String
    ) : this(nameAndId, nameAndId, email)
}
