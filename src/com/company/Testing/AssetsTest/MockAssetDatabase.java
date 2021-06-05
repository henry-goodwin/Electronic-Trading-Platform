package com.company.Testing.AssetsTest;

import com.company.Common.DataSource.AssetDataSource;
import com.company.Common.Model.Asset;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MockAssetDatabase implements AssetDataSource {

    TreeMap<Integer, Asset> data;

    public MockAssetDatabase() {
        data = new TreeMap<Integer, Asset>();
        try {
            data.put(data.size()+1, new Asset(data.size()+1, "Storage"));
            data.put(data.size()+1, new Asset(data.size()+1, "Rendering"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add asset to the database
     * @param asset Asset to add
     * @throws Exception Throw exception if fails
     */
    @Override
    public void addAsset(Asset asset) throws Exception {
        if (asset.getName().equals("")) throw new Exception("Empty Asset Name");

        data.put(data.size()+1, asset);
    }

    /**
     * Get Asset from the database
     * @param assetID The assetID as a Integer to search for.
     * @return return asset with the ID
     * @throws Exception throws exception if getAsset fails
     */
    @Override
    public Asset getAsset(Integer assetID) throws Exception {
        if (assetID < 0) throw new Exception();

        return data.get(assetID);

    }

    /**
     * Check that asset name exists in database, used to ensure duplicates are not entered
     * @param string name to search for
     * @return Return true if name is free, return false if name is taken
     * @throws Exception throw exception if fails
     */
    @Override
    public Boolean checkName(String string) throws Exception {
        if (string.equals("")) throw new Exception("Error: Empty Name");

        for (Integer key: data.keySet()) {
            if (data.get(key).getName().equals(string)) return true;
        }

        return false;

    }

    @Override
    public void close() {

    }

    /**
     * Get set of all assets
     * @return asset set
     * @throws Exception throw exception if fails
     */
    @Override
    public Set<Asset> assetSet() throws Exception {
        Set<Asset> userSet = new TreeSet<Asset>();

        for(Integer key: data.keySet()) {
            userSet.add(data.get(key));
        }
        return userSet;
    }

    /**
     * Update the name of asset in database
     * @param assetID assetID to search for
     * @param name name to update
     * @throws Exception Throws exception if fails
     */
    @Override
    public void updateAssetName(Integer assetID, String name) throws Exception {
        if (assetID < 0) throw new Exception("Negative Asset ID");
        if (name.equals("")) throw new Exception("Error: Empty name");
        data.get(assetID).setName(name);
    }

    @Override
    public void deleteAsset(Integer assetID) {

    }
}
