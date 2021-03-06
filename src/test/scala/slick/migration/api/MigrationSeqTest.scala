package scala.slick
package migration.api

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import scala.slick.driver.H2Driver

class MigrationSeqTest extends FunSuite with ShouldMatchers {
  test("& returns the right type and doesn't keep nesting") {
    val m = new Migration {
      def apply()(implicit s: H2Driver.simple.Session) = ()
    }
    m & m & m should equal (MigrationSeq(m, m, m))

    val rm = new ReversibleMigration {
      def apply()(implicit s: H2Driver.simple.Session) = ()
      def reverse = this
    }

    val rms = rm & rm & rm
    implicitly[rms.type <:< ReversibleMigrationSeq]
    rms should equal (new ReversibleMigrationSeq(rm, rm, rm))
  }
}
