#ifndef LIBTGVOIP_PULSE_FUNCTIONS_H
#define LIBTGVOIP_PULSE_FUNCTIONS_H

#define pa_threaded_mainloop_new AudioPulse::_import_pa_threaded_mainloop_new
#define pa_threaded_mainloop_get_api AudioPulse::_import_pa_threaded_mainloop_get_api
#define pa_context_new AudioPulse::_import_pa_context_new
#define pa_context_new_with_proplist AudioPulse::_import_pa_context_new_with_proplist
#define pa_context_set_state_callback AudioPulse::_import_pa_context_set_state_callback
#define pa_threaded_mainloop_lock AudioPulse::_import_pa_threaded_mainloop_lock
#define pa_threaded_mainloop_unlock AudioPulse::_import_pa_threaded_mainloop_unlock
#define pa_threaded_mainloop_start AudioPulse::_import_pa_threaded_mainloop_start
#define pa_context_connect AudioPulse::_import_pa_context_connect
#define pa_context_get_state AudioPulse::_import_pa_context_get_state
#define pa_threaded_mainloop_wait AudioPulse::_import_pa_threaded_mainloop_wait
#define pa_stream_new_with_proplist AudioPulse::_import_pa_stream_new_with_proplist
#define pa_stream_set_state_callback AudioPulse::_import_pa_stream_set_state_callback
#define pa_stream_set_write_callback AudioPulse::_import_pa_stream_set_write_callback
#define pa_stream_connect_playback AudioPulse::_import_pa_stream_connect_playback
#define pa_operation_unref AudioPulse::_import_pa_operation_unref
#define pa_stream_cork AudioPulse::_import_pa_stream_cork
#define pa_threaded_mainloop_stop AudioPulse::_import_pa_threaded_mainloop_stop
#define pa_stream_disconnect AudioPulse::_import_pa_stream_disconnect
#define pa_stream_unref AudioPulse::_import_pa_stream_unref
#define pa_context_disconnect AudioPulse::_import_pa_context_disconnect
#define pa_context_unref AudioPulse::_import_pa_context_unref
#define pa_threaded_mainloop_free AudioPulse::_import_pa_threaded_mainloop_free
#define pa_threaded_mainloop_signal AudioPulse::_import_pa_threaded_mainloop_signal
#define pa_stream_begin_write AudioPulse::_import_pa_stream_begin_write
#define pa_stream_write AudioPulse::_import_pa_stream_write
#define pa_strerror AudioPulse::_import_pa_strerror
#define pa_stream_get_state AudioPulse::_import_pa_stream_get_state
#define pa_stream_set_read_callback AudioPulse::_import_pa_stream_set_read_callback
#define pa_stream_connect_record AudioPulse::_import_pa_stream_connect_record
#define pa_stream_peek AudioPulse::_import_pa_stream_peek
#define pa_stream_drop AudioPulse::_import_pa_stream_drop
#define pa_mainloop_new AudioPulse::_import_pa_mainloop_new
#define pa_mainloop_get_api AudioPulse::_import_pa_mainloop_get_api
#define pa_mainloop_iterate AudioPulse::_import_pa_mainloop_iterate
#define pa_mainloop_free AudioPulse::_import_pa_mainloop_free
#define pa_context_get_sink_info_list AudioPulse::_import_pa_context_get_sink_info_list
#define pa_context_get_source_info_list AudioPulse::_import_pa_context_get_source_info_list
#define pa_operation_get_state AudioPulse::_import_pa_operation_get_state
#define pa_proplist_new AudioPulse::_import_pa_proplist_new
#define pa_proplist_sets AudioPulse::_import_pa_proplist_sets
#define pa_proplist_free AudioPulse::_import_pa_proplist_free

#endif //LIBTGVOIP_PULSE_FUNCTIONS_H