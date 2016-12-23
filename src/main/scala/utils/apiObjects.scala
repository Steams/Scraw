package scraw.utils

package apiObjects {

  class BaseObject(val id : String, val name : String)

  class Listing(val data : List[BaseObject])

  trait Votable {
    def ups : Int
    def downs : Int
    def likes : Boolean
  }

  trait Created {
    def created : Long
    def created_utc : Long
  }

}
