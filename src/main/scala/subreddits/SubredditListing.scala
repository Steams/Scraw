import scraw.utils.apiObjects.{Listing, BaseObject}

class SubredditListing (
    override val data :List[BaseObject]
  ) extends Listing(data) {

}
