package com.company.Testing.AssetsTest;

import com.company.Common.Model.Asset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAsset {

    Asset asset;
    MockAssetDatabase mockAssetDatabase;

    @BeforeEach
    void ConstructAsset() throws Exception {
        asset = new Asset(4, "CPU");
    }

    @BeforeEach
    void ConstructDatabase() throws Exception {
        mockAssetDatabase = new MockAssetDatabase();
    }

    @Test
    void TestSetAssetNameValid() throws Exception {}

    @Test
    void TestSetAssetNameInvalid() throws Exception {}

    @Test
    void TestSetAssetIDValid() throws Exception {}

    @Test
    void TestSetAssetIDInvalid() throws Exception {}

    @Test
    void TestSetAssetIDLargeNumber() throws Exception {}

    @Test
    void TestAddAsset() throws Exception {}

    @Test
    void TestGetAsset() throws Exception {}

    @Test
    void TestCheckFreeName() throws Exception {}

    @Test
    void TestCheckTakenName() throws Exception {}

    @Test
    void TestUpdateValidAsset() throws Exception {}

    @Test
    void TestUpdateInvalidAsset() throws Exception {}

}
