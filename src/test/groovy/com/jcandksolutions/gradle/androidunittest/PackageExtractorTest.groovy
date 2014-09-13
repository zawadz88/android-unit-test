package com.jcandksolutions.gradle.androidunittest

import com.android.build.gradle.internal.ProductFlavorData
import com.android.build.gradle.internal.api.DefaultAndroidSourceSet
import com.android.builder.core.DefaultProductFlavor

import org.junit.Before
import org.junit.Test

import static org.fest.assertions.api.Assertions.assertThat
import static org.fest.assertions.api.Fail.fail
import static org.mockito.Mockito.when

public class PackageExtractorTest {
  private PackageExtractor mTarget
  private MockProvider mProvider
  private ProductFlavorData mData

  @Before
  public void setUp() {
    mProvider = new MockProvider()
    mData = mProvider.provideDefaultConfigData()
    mTarget = new PackageExtractor(mData, mProvider.provideLogger())
  }

  @Test
  public void testGetPackageNameWithProvidedAppId() {
    DefaultProductFlavor flavor = mData.productFlavor
    when(flavor.applicationId).thenReturn("package")
    assertThat(mTarget.packageName).isEqualTo("package")
  }

  @Test
  public void testGetPackageNameFromManifest() {
    DefaultAndroidSourceSet source = mData.sourceSet
    when(source.manifestFile).thenReturn(new File("package"))
    try {
      mTarget.packageName
      fail("Should throw FileNotFoundException")
    } catch (RuntimeException ignored) {
      assertThat(ignored).hasMessageStartingWith("java.io.FileNotFoundException: package")
    }
  }
}
