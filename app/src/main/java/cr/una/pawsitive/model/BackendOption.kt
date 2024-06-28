package cr.una.pawsitive.model

data class BackendOption(val label: String, val value: String) {
    override fun toString(): String {
        return label
    }
}