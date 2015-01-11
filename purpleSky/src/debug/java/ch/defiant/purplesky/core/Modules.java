package ch.defiant.purplesky.core;

/**
 * @author Patrick Bänziger
 */
final class Modules {

    public static Object[] list(PurpleSkyApplication appContext){
        return new Object[] {
                new PurpleSkyModule(appContext),
                new DebugModule()
        };
    }

    private Modules() {
        // No instances.
    }
}
