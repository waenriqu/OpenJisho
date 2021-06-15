package com.gaumala.openjisho.frontend.radicals

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gaumala.openjisho.backend.db.DictDatabase
import com.gaumala.openjisho.frontend.dict.DictSavedState
import com.gaumala.mvi.Dispatcher

class RadicalsViewModelFactory(private val f: Fragment): ViewModelProvider.Factory {

    private fun getInitialState(): RadicalsState {
        val dictSavedState: DictSavedState? =
            f.arguments!!.getParcelable(RadicalsFragment.DICT_SAVED_STATE_KEY)

        return RadicalsState(
            radicals = RadicalIndex.listAll(),
            results = KanjiResults.Ready(emptyList()),
            queryText = dictSavedState?.queryText ?: "")
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val ctx = f.activity as Context

        val appDB = DictDatabase.getInstance(ctx)
        val seRunner = RadicalsSERunner(appDB.dictQueryDao())

        val initialState = getInitialState()
        val newDispatcher = Dispatcher(seRunner, initialState)

        val viewModel = RadicalsViewModel()
        viewModel.setDispatcher(newDispatcher)
        return viewModel as T
    }

}