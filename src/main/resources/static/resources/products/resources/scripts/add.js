ClassicEditor
    .create(window.document.getElementById('editor'), {
        simpleUpload: {
            uploadUrl: '/product/add/upload-image'
        }
    }).then(editor => {
    editor.editing.view.change(writer => {
        writer.setStyle('min-height', '20rem', editor.editing.view.document.getRoot());
        writer.setStyle('max-height', '70vh', editor.editing.view.document.getRoot());
    })
});