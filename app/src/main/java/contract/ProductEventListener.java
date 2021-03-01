package contract;

public interface ProductEventListener {

    // FirebaseFireStore
    void insertDataToFirestore( String id, String image, String title, String desc, String price, String rating );

    void deleteDataFromFirestore( int position, String id, String image, String title, String desc, String price, String rating );

    // get data from firestore
    void getDataFromFirestore();

}
