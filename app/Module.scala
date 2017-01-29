import com.google.inject.AbstractModule
import search.{InsertTestData, SearchDB}

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[SearchDB]).asEagerSingleton()
    bind(classOf[InsertTestData]).asEagerSingleton()
  }

}
