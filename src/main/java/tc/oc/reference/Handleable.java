package tc.oc.reference;

/**
 * An object that can supply a {@link Handle} for itself.
 *
 * When creating a handle that uses an anonymous class or lambda to retrieve the referent,
 * be careful not to capture the referent itself. For example, this is bad:
 *
 * <code>
 *     class Thing implements Handleable {
 *         int id;
 *
 *         @Override
 *         public Handle<? extends Thing> handle() {
 *             return Handle.ofSupplier(() -> Thing.find(this.id))
 *         }
 *     }
 * </code>
 *
 * The lambda captures a reference to the Thing by accessing an instance field,
 * and so the Handle effectively leaks the instance. To ensure this doesn't happen,
 * you can create the reference in a static method that doesn't have any access
 * to the referent:
 *
 * <code>
 *         private static Handle<? extends Thing> makeHandle(int id) {
 *             return Handle.ofSupplier(() -> Thing.find(id));
 *         }
 *
 *         @Override
 *         public Handle<? extends Thing> handle() {
 *             return makeHandle(this.id);
 *         }
 * </code>
 *
 */
public interface Handleable {

    default Handle<?> handle() {
        return Handle.ofWeakInstance(this);
    }
}
