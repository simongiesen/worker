package me.raatiniemi.worker.base.view;

import android.app.Fragment;

import me.raatiniemi.worker.base.presenter.MvpPresenter;

abstract public class MvpFragment<P extends MvpPresenter> extends Fragment implements MvpView {
    /**
     * Instance for the presenter.
     */
    private P mPresenter;

    /**
     * Create the instance for the presenter.
     *
     * @return Instance for the presenter.
     */
    abstract protected P createPresenter();

    /**
     * Retrieve the presenter, create instance if none is available.
     *
     * @return Instance for the presenter.
     */
    protected P getPresenter() {
        if (null == mPresenter) {
            mPresenter = createPresenter();
        }
        return mPresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (null != getPresenter()) {
            getPresenter().detachView();
        }
    }
}