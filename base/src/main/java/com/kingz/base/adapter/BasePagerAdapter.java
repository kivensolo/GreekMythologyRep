package com.kingz.base.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 在FragmentPagerAdapter的instantiateItem方法中（这个方法会在ViewPager滑动状态变更时调用），
 * 每个position所对应的Fragment只会添加一次到FragmentManager里面，也就是说，
 * 所以重写的getItem方法，position参数不会出现两次相同的值(缓存原因)。
 *
 * 当Fragment被添加时，会给这个Fragment指定一个根据itemId来区分的tag，
 * 而这个itemId就是根据getItemId方法来获取的，
 * 默认就是当前页面的索引值。
 *
 * 关键点就一点，getItem 这个方法不是 get Fragment，其实称之为 create Fragment更为合适。
 * 原理参考：https://mp.weixin.qq.com/s/MOWdbI5IREjQP1Px-WJY1Q
 *
 * @param <D> 数据源类型
 */
public class BasePagerAdapter<D> extends FragmentPagerAdapter {

    // 数据集合
    private List<D> mData;

    // Fragment和title的构建器
    private PagerFragCreator<D> mCreator;

    // 已实例化的Fragment缓存
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public BasePagerAdapter(FragmentManager fm, PagerFragCreator<D> creator) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mCreator = creator;
    }

    public void setData(List<D> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public boolean hasData(){
        return mData != null && mData.size() > 0;
    }

    /**
     * 这里实际是用于创建新的Fragment，而不是获取。
     * 在ViewPager2中，已经改为了createFragment
     *
     * @param position  fragment所在的position
     * @return The Fragment associated with a specified position
     */
    @Override
    public Fragment getItem(int position) {
        return mCreator.createFragment(mData.get(position), position);
    }

    /**
     * 自定义获取缓存中的fragment
     */
    public Fragment getFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 此函数会调用getItem()函数，返回新的函数，新的对象将会被FragmentTransaction.add().FragmentStatePagerAdapter就是通过这种方式
     * 每一次都会创建新的Fragment，而在不需要的情况下立刻释放资源，来达到节省内存的目的
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * 移除Fragment，此时使用的FragementTransaction.remove(),并释放其资源：
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //除registeredFragments缓存的fragment
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mCreator.createTitle(mData.get(position));
    }

    public interface PagerFragCreator<T> {

        Fragment createFragment(T data, int position);

        String createTitle(T data);
    }
}
