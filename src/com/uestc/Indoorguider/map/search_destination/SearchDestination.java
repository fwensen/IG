package com.uestc.Indoorguider.map.search_destination;

import com.uestc.Indoorguider.R;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SearchDestination extends LinearLayout implements View.OnClickListener{
	
	
	private EditText etInputText; //搜索框
	private ImageView ivDelete;   //删除输入
	private Button btnSearch;     //search按钮
	private Context context;      //上下文对象
	private ListView lvContent;   //弹出列表
	private ArrayAdapter<String> mHintAdapter;  //提示
    private ArrayAdapter<String> mAutoCompleteAdapter;  //自动补全
    private SearchViewListener mListener;   //回调接口，搜索功能
    
	public SearchDestination(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
        LayoutInflater.from(context).inflate(R.layout.search_destination, this);
        init();
	}

	
	private void init() {
		etInputText = (EditText) findViewById(R.id.search_et_input);
		etInputText.setFocusable(false);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        btnSearch = (Button) findViewById(R.id.search_btn_back);
        lvContent = (ListView) findViewById(R.id.search_lv_tips);
        
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //set edit text
                String text = lvContent.getAdapter().getItem(i).toString();
                etInputText.setText(text);
                etInputText.setSelection(text.length());
                //hint list view gone and result list view show
                lvContent.setVisibility(View.GONE);
                notifyStartSearching(text);
            }
        });

        ivDelete.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        etInputText.addTextChangedListener(new EditChangedListener());
        etInputText.setOnClickListener(this);
        etInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                	lvContent.setVisibility(GONE);
                    notifyStartSearching(etInputText.getText().toString());
                }
                return true;
            }
        });
	}
	
	private void notifyStartSearching(String text){
		 if (mListener != null) {
	            mListener.onSearch(etInputText.getText().toString());
	        }
	        //隐藏软键盘
	     //InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	     //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	 }
	
	
	 /**
     * 设置热搜版提示 adapter
     */
    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
        this.mHintAdapter = adapter;
        if (lvContent.getAdapter() == null) {
        	lvContent.setAdapter(mHintAdapter);
        }
    }

	
	public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
        this.mAutoCompleteAdapter = adapter;
    }
	
	 public void setSearchViewListener(SearchViewListener listener) {
	        mListener = listener;
	    }

	 private class EditChangedListener implements TextWatcher {
	        @Override
	        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

	        }

	        @Override
	        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
	            if (!"".equals(charSequence.toString())) {
	                ivDelete.setVisibility(VISIBLE);
	                lvContent.setVisibility(VISIBLE);
	                if (mAutoCompleteAdapter != null && lvContent.getAdapter() != mAutoCompleteAdapter) {
	                	lvContent.setAdapter(mAutoCompleteAdapter);
	                }
	                //更新autoComplete数据
	                if (mListener != null) {
	                    mListener.onRefreshAutoComplete(charSequence + "");
	                }
	            } else {
	                ivDelete.setVisibility(GONE);
	                if (mHintAdapter != null) {
	                	lvContent.setAdapter(mHintAdapter);
	                }
	                lvContent.setVisibility(GONE);
	            }

	        }

	        @Override
	        public void afterTextChanged(Editable editable) {
	        }
	    }
	
	/**
	 * 回调接口
	 */
	public interface SearchViewListener {

		//自动补全内容
        void onRefreshAutoComplete(String text);

        //开始搜索
        void onSearch(String text);

    }
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		/**
		 * 点击搜索框
		 */
        case R.id.search_et_input:
        	if (lvContent.isShown()) {
        		lvContent.setVisibility(GONE);
        	} else {
        		lvContent.setVisibility(VISIBLE);
        	}
        	
            break;
        /**
         * 点击删除小图标
         */
        case R.id.search_iv_delete:
            etInputText.setText("");
            ivDelete.setVisibility(GONE);
            break;
        /**
         * 点击出发
         */
        case R.id.search_btn_back:
        	//mListener.onSearch(etInputText.getText().toString());
        	notifyStartSearching(null);
        	etInputText.setText("");
            break;
		}
	}

}