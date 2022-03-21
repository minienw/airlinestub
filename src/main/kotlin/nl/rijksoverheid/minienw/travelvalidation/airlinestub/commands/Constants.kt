package nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands

//TODO the usual dumping ground...
class Constants {
    companion object {
        /**
         TODO Also check max from current time + 1 day???
         * */
        const val LowestPossibleTime : Long = 1645966339L

        const val GuidAsHexRegexPattern = "^[0-9A-Fa-f]{32}$" //TODO \\?

        //TODO Use GET identity to publish the accepted one
        //val permittedWalletSigningAlgorithms : Array<String> = arrayOf("ES256", "PS256", "RS256") //TODO use a resolver instead?
    }
}