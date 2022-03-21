package api.coordinate

/**
 * A POM-compliant License object.
 */
data class License(
    val name: String,
    val url: String,
    val distribution: String = "repo",
)

/**
 * A POM-compliant developer object.
 */
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
