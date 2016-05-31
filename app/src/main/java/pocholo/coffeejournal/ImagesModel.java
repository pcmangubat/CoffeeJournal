package pocholo.coffeejournal;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ImagesModel {
    public Integer resultCount;

    @SerializedName("images")
    public List<Image> images = new ArrayList<Image>();

    public class Image {

        @SerializedName("id")
        public String id;

        @SerializedName("asset_family")
        public String assetFamily;

        @SerializedName("caption")
        public Object caption;

        @SerializedName("collection_code")
        public String collectionCode;

        @SerializedName("collection_id")
        public Integer collectionId;

        @SerializedName("collection_name")
        public String collectionName;

        @SerializedName("display_sizes")
        public List<DisplaySize> displaySizes = new ArrayList<DisplaySize>();

        @SerializedName("license_model")
        public String licenseModel;

        @SerializedName("title")
        public String title;

    }

    public class DisplaySize {

        @SerializedName("is_watermarked")
        public Boolean isWatermarked;

        @SerializedName("name")
        public String name;

        @SerializedName("uri")
        public String uri;

    }
}