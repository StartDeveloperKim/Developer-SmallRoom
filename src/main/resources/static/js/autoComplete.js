export async function getAutoComplete(query) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: "GET",
            url: "/api/autocomplete?query=" + query,
            timeout: 3000,
            success: function (data) {
                resolve(data);
            },
            error: function (data, status, err) {
                reject(err);
            }
        });
    });
}