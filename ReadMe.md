# Retrofit combie with RxJava to compare with Volley.

![run.gif](http://upload-images.jianshu.io/upload_images/2833342-047c9c86d22438a9.gif?imageMogr2/auto-orient/strip)

By get same data form a same api(zhi hu stories)

> ### main code

    //get data by Retrofit & RxJava
    private void getDataByRetrofit() {
        progressBar.setVisibility(View.VISIBLE);
        storiesAdapter.setGetPicByRR(true);// tell adapter get pic by retrofit
        Subscriber<Result> subscriber = new Subscriber<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {
                super.onStart();
                storiesList.clear();
            }

            @Override
            public void onNext(Result result) {
                storiesList.addAll(result.getStories());
                storiesAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        GetNews getNews = RetrofitManager.getInstance().getRetrofit().create(GetNews.class);
        getNews.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

---

    /**
     * get data by volley
     */
    private void getDataByVolley() {
        progressBar.setVisibility(View.VISIBLE);
        storiesAdapter.setGetPicByRR(false);// tell adapter get pic by volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                RetrofitManager.BASE_URL + "latest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        storiesList.addAll(gson.fromJson(response, Result.class).getStories());
                        storiesAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyManager.getInstance().getRequestQueue(this.getApplication()).add(stringRequest);
    }