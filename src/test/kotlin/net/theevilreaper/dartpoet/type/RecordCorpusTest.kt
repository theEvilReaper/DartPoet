package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDef
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DOUBLE
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.RecordTypeName
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Corpus tests for Dart 3 record types verified against the Dart analyzer")
class RecordCorpusTest {

    @Test
    fun `test top level record properties in a Dart file`() {
        val file = DartFile.builder("record_properties")
            .property(
                PropertySpec.builder("pair", RecordTypeName.of(INTEGER, STRING))
                    .modifier { DartModifier.FINAL }
                    .initWith("%L", "(1, 'hello')")
                    .build()
            )
            .property(
                PropertySpec.builder(
                    "user",
                    RecordTypeName.builder().named("id", INTEGER).named("name", STRING).build()
                )
                    .modifier { DartModifier.FINAL }
                    .initWith("%L", "(id: 42, name: 'Alice')")
                    .build()
            )
            .property(
                PropertySpec.builder("single", RecordTypeName.of(INTEGER))
                    .modifier { DartModifier.FINAL }
                    .initWith("%L", "(1,)")
                    .build()
            )
            .function(
                FunctionSpec.builder("main")
                    .addCode("print(pair);\nprint(user);\nprint(single);")
                    .build()
            )
            .build()

        file.verifyDartOutput(
            """
            |final (int, String) pair = (1, 'hello');
            |final ({int id, String name}) user = (id: 42, name: 'Alice');
            |final (int,) single = (1,);
            |
            |void main() {
            |  print(pair);
            |  print(user);
            |  print(single);
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test functions with record parameters and return types`() {
        val swapFunction = FunctionSpec.builder("swap")
            .returns(RecordTypeName.of(INTEGER, STRING))
            .parameter(ParameterSpec.positional("a", STRING).build())
            .parameter(ParameterSpec.positional("b", INTEGER).build())
            .addCode("return (b, a);")
            .build()

        val calcFunction = FunctionSpec.builder("calculate")
            .returns(
                RecordTypeName.builder()
                    .named("sum", INTEGER)
                    .named("diff", INTEGER)
                    .build()
            )
            .parameter(ParameterSpec.positional("a", INTEGER).build())
            .parameter(ParameterSpec.positional("b", INTEGER).build())
            .addCode("return (sum: a + b, diff: a - b);")
            .build()

        val file = DartFile.builder("record_functions")
            .function(swapFunction)
            .function(calcFunction)
            .build()

        file.verifyDartOutput(
            """
            |(int, String) swap(String a, int b) {
            |  return (b, a);
            |}
            |
            |({int sum, int diff}) calculate(int a, int b) {
            |  return (sum: a + b, diff: a - b);
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test class containing record properties and methods`() {
        val coordsType = RecordTypeName.builder()
            .positional(DOUBLE, "lat")
            .positional(DOUBLE, "lng")
            .build()

        val clazz = ClassSpec.builder("Location")
            .property(
                PropertySpec.builder("coordinates", coordsType)
                    .modifier { DartModifier.FINAL }
                    .build()
            )
            .constructor(
                ConstructorSpec.builder("Location")
                    .parameters(ParameterSpec.positional("coordinates").build())
                    .build()
            )
            .function(
                FunctionSpec.builder("getRawCoords")
                    .returns(RecordTypeName.of(DOUBLE, DOUBLE))
                    .addCode("return coordinates;")
                    .build()
            )
            .build()

        val file = DartFile.builder("location_model")
            .type(clazz)
            .build()

        file.verifyDartOutput(
            """
            |class Location {
            |
            |  final (double lat, double lng) coordinates;
            |
            |  Location(this.coordinates);
            |
            |  (double, double) getRawCoords() {
            |    return coordinates;
            |  }
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test file with record typedefs and parameterized collections`() {
        val point3dTypeDef = TypeDef.alias("Point3D")
            .returns(
                RecordTypeName.builder()
                    .named("x", DOUBLE)
                    .named("y", DOUBLE)
                    .named("z", DOUBLE)
                    .build()
            )
            .build()

        val listProperty = PropertySpec.builder(
            "indexedItems",
            ClassName("List").parameterizedBy(RecordTypeName.of(INTEGER, STRING))
        )
            .modifier { DartModifier.FINAL }
            .initWith("%L", "<(int, String)>[(1, 'one'), (2, 'two')]")
            .build()

        val file = DartFile.builder("record_typedefs")
            .typeDef(point3dTypeDef)
            .property(listProperty)
            .build()

        file.verifyDartOutput(
            """
            |typedef Point3D = ({double x, double y, double z});
            |
            |final List<(int, String)> indexedItems = <(int, String)>[(1, 'one'), (2, 'two')];
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test standalone record properties`() {
        val positionalProp = PropertySpec.builder("pair", RecordTypeName.of(INTEGER, STRING))
            .modifier { DartModifier.LATE }
            .build()
        positionalProp.verifyDartOutput("late (int, String) pair;")

        val singleProp = PropertySpec.builder("single", RecordTypeName.of(INTEGER))
            .modifier { DartModifier.LATE }
            .build()
        singleProp.verifyDartOutput("late (int,) single;")

        val namedProp = PropertySpec.builder(
            "point",
            RecordTypeName.builder().named("x", DOUBLE).named("y", DOUBLE).build()
        )
            .modifier { DartModifier.LATE }
            .build()
        namedProp.verifyDartOutput("late ({double x, double y}) point;")

        val mixedNullableProp = PropertySpec.builder(
            "userTuple",
            RecordTypeName.builder().positional(INTEGER, "id").named("name", STRING).nullable().build()
        ).build()
        mixedNullableProp.verifyDartOutput("(int id, {String name})? userTuple;")
    }

    @Test
    fun `test standalone external functions with records`() {
        val returningRecord = FunctionSpec.builder("getCoordinates")
            .modifiers(DartModifier.EXTERNAL)
            .returns(RecordTypeName.builder().positional(DOUBLE, "lat").positional(DOUBLE, "lng").build())
            .build()
        returningRecord.verifyDartOutput("external (double lat, double lng) getCoordinates();")

        val takingRecord = FunctionSpec.builder("registerUser")
            .modifiers(DartModifier.EXTERNAL)
            .parameter(
                ParameterSpec.positional(
                    "info",
                    RecordTypeName.builder().named("name", STRING).named("age", INTEGER).build()
                ).build()
            )
            .build()
        takingRecord.verifyDartOutput("external void registerUser(({String name, int age}) info);")
    }

    @Test
    fun `test standalone record typedefs`() {
        val pairTypeDef = TypeDef.alias("Pair")
            .returns(RecordTypeName.of(INTEGER, STRING))
            .build()
        pairTypeDef.verifyDartOutput("typedef Pair = (int, String);")

        val userRecordTypeDef = TypeDef.alias("UserRecord")
            .returns(
                RecordTypeName.builder().named("id", INTEGER).named("name", STRING).build()
            )
            .build()
        userRecordTypeDef.verifyDartOutput("typedef UserRecord = ({int id, String name});")
    }
}
